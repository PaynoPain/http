package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

import java.util.Date;

public class CacheRequester implements ResourceRequester {
    private final ResourceRequester requester;
    private final CacheStorage cache;
    private final Factory<Date> timeGateway;
    private final Long cacheDurationInMilliseconds;

    public CacheRequester(ResourceRequester resourceRequester, CacheStorage storage, Factory<Date> timeGateway, Long cacheDurationInMilliseconds) {
        requester = resourceRequester;
        cache = storage;
        this.timeGateway = timeGateway;
        this.cacheDurationInMilliseconds = cacheDurationInMilliseconds;
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
            } catch (Throwable ignored){}

            if (entry != null)
                cache.write(req, entry);
        }

        return cache.read(req).response;
    }

    private CacheEntry runRequest(Request req) {
        Response response = requester.run(req);
        Date expiration = new Date(now().getTime() + cacheDurationInMilliseconds);
        return new CacheEntry(expiration, response);
    }

    private boolean isOutdated(CacheEntry cacheEntry) {
        return now().after(cacheEntry.expiration);
    }

    private Date now() {
        return timeGateway.get();
    }
}
