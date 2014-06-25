package com.touchiteasy.http.cache;

public class CacheStorageInMemoryTest extends CacheStorageContract {
    @Override
    public CacheStorage createCacheStorage() {
        return new CacheStorageInMemory();
    }
}
