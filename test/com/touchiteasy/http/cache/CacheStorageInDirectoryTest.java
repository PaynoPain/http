package com.touchiteasy.http.cache;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class CacheStorageInDirectoryTest extends CacheStorageContract {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public CacheStorage createCacheStorage() {
        return new CacheStorageInDirectory(testFolder.getRoot());
    }
}
