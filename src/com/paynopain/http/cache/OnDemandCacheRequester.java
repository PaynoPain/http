package com.paynopain.http.cache;

import com.paynopain.commons.Factory;
import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.Response;

import java.util.Date;

public class OnDemandCacheRequester implements ResourceRequester {
    private final ResourceRequester requester;
    private final MapStorage<Request, CacheEntry> cache;
    private final Factory<Date> timeGateway;
    private final Long cacheDurationInMilliseconds;
    private final Long timeToRefreshInMilliseconds;

    public OnDemandCacheRequester(
            ResourceRequester resourceRequester, MapStorage<Request, CacheEntry> storage, Factory<Date> timeGateway,
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
            cache.write(req, createEntry(requester.run(req)));
        }

        if (isOutdated(cache.read(req))){
            CacheEntry entry = null;

            try {
                entry = createEntry(requester.run(req));
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

    private CacheEntry createEntry(Response response) {
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

    public void expire(Request req) {
        if (cache.contains(req)){
            final CacheEntry entry = cache.read(req);
            Date before = new Date(now().getTime() -1);
            cache.write(req, new CacheEntry(entry.response, before, entry.deadline));
        }
    }
}
