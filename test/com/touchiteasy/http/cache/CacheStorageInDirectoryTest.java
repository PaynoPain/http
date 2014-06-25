package com.touchiteasy.http.cache;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class CacheStorageInDirectoryTest extends CacheStorageContract {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public CacheStorage createCacheStorage() {
        return new CacheStorageInDirectory(testFolder.getRoot());
    }
}
