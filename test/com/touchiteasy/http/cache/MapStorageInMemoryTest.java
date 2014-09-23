package com.touchiteasy.http.cache;

public class MapStorageInMemoryTest extends CacheStorageContract {
    @Override
    public MapStorage createCacheStorage() {
        return new MapStorageInMemory();
    }
}
