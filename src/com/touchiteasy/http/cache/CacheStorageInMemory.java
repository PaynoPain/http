package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class CacheStorageInMemory implements CacheStorage {
    private Map<Request, CacheEntry> cache = new HashMap<Request, CacheEntry>();

    public CacheEntry read(Request req){
        return cache.get(new HashedRequest(req));
    }

    public boolean contains(Request req){
        return cache.containsKey(new HashedRequest(req));
    }

    public void write(Request req, CacheEntry entry){
        cache.put(new HashedRequest(req), entry);
    }
}
