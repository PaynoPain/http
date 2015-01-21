package com.paynopain.http.cache;

import com.paynopain.http.IdentifiableRequest;
import com.paynopain.http.Request;

import java.util.HashMap;
import java.util.Map;

public class MapStorageInMemory implements MapStorage<Request, CacheEntry> {
    private Map<Request, CacheEntry> cache = new HashMap<Request, CacheEntry>();

    public CacheEntry read(Request req){
        if (!contains(req))
            throw new IllegalStateException("There is no cached entry for the resource: " + req.getResource());

        return cache.get(new IdentifiableRequest(req));
    }

    public boolean contains(Request req){
        return cache.containsKey(new IdentifiableRequest(req));
    }

    public void write(Request req, CacheEntry entry){
        cache.put(new IdentifiableRequest(req), entry);
    }
}
