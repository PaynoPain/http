package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class CacheStorageInMemory implements CacheStorage {
    private Map<Request, CacheEntry> cache = new HashMap<Request, CacheEntry>();

    public CacheEntry read(Request req){
        return cache.get(req);
    }

    public boolean contains(Request req){
        return cache.containsKey(req);
    }

    public void write(Request req, CacheEntry entry){
        cache.put(req, entry);
    }
}
