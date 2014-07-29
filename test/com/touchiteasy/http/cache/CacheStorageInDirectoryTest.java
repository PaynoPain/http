package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CacheStorageInDirectoryTest extends CacheStorageContract {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public CacheStorage createCacheStorage() {
        return new CacheStorageInDirectory(testFolder.getRoot());
    }

    @Test
    public void GivenACorruptedFile_ShouldDeleteIt() throws IOException {
        createCorruptedFile(request, entry);
        assertThat(storage.contains(request), is(false));
        assertThat(testFolder.getRoot().list().length, is(0));
    }

    private void createCorruptedFile(Request request, CacheEntry entry) throws IOException {
        storage.write(request, entry);

        File cacheEntryFile = testFolder.getRoot().listFiles()[0];

        final FileWriter writer = new FileWriter(cacheEntryFile);
        writer.write("");
        writer.close();
    }
}
