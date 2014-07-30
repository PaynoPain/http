package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

import java.util.Date;

public class OnDemandCacheRequester implements ResourceRequester {
    private final ResourceRequester requester;
    private final CacheStorage cache;
    private final Factory<Date> timeGateway;
    private final Long cacheDurationInMilliseconds;
    private final Long timeToRefreshInMilliseconds;

    public OnDemandCacheRequester(
            ResourceRequester resourceRequester, CacheStorage storage, Factory<Date> timeGateway,
            Long cacheDurationInMilliseconds, Long timeToRefreshAfterCacheExpirationInMilliseconds) {
        requester = resourceRequester;
        cache = storage;
        this.timeGateway = timeGateway;
        this.cacheDurationInMilliseconds = cacheDurationInMilliseconds;
        this.timeToRefreshInMilliseconds = timeToRefreshAfterCacheExpirationInMilliseconds;
    }

    @Override
    public Response run(Request req) {
        if (!cache.contains(req)){
            cache.write(req, runRequest(req));
        }

        if (isOutdated(cache.read(req))){
            CacheEntry entry = null;

            try {
                entry = runRequest(req);
            } catch (RuntimeException e){
                if (isDeadline(cache.read(req))){
                    throw e;
                }
            }

            if (entry != null)
                cache.write(req, entry);
        }

        return cache.read(req).response;
    }

    private CacheEntry runRequest(Request req) {
        Response response = requester.run(req);

        long now = now().getTime();
        long expiration = now + cacheDurationInMilliseconds;
        long deadline = expiration + timeToRefreshInMilliseconds;

        return new CacheEntry(response, new Date(expiration), new Date(deadline));
    }

    private boolean isOutdated(CacheEntry cacheEntry) {
        return now().after(cacheEntry.expiration);
    }

    private boolean isDeadline(CacheEntry cacheEntry) {
        return now().after(cacheEntry.deadline);
    }

    private Date now() {
        return timeGateway.get();
    }
}
