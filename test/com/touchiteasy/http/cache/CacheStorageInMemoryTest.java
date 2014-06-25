package com.touchiteasy.http.cache;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class CacheStorageInMemoryTest extends CacheStorageContract {
    @Override
    public CacheStorage createCacheStorage() {
        return new CacheStorageInMemory();
    }
}
