package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

public interface CacheStorage {
    public boolean contains(Request req);
    public CacheEntry read(Request req);
    public void write(Request req, CacheEntry entry);
}