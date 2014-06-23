package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class CacheStorageInMemory implements CacheStorage {
    private static class HashedRequest implements Request {
        private Request base;

        public HashedRequest(Request base){
            this.base = base;
        }

        @Override
        public String getResource() {
            return this.base.getResource();
        }

        @Override
        public Map<String, String> getParameters() {
            return this.base.getParameters();
        }

        @Override
        public int hashCode() {
            return this.base.getResource().hashCode() + this.base.getParameters().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Request &&
                    this.base.getResource().equals(((Request) obj).getResource()) &&
                    this.base.getParameters().equals(((Request) obj).getParameters());
        }
    }

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
