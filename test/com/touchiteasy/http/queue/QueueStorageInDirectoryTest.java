package com.touchiteasy.http.queue;

import com.touchiteasy.commons.Function;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class QueueStorageInDirectoryTest extends QueueStorageContract {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public QueueStorage<String> createInstance() {
        return new QueueStorageInDirectory<String>(
                testFolder.getRoot(),
                new Function<String, String>() {
                    @Override
                    public String apply(String s) throws RuntimeException {
                        return s;
                    }
                },
                new Function<String, String>() {
                    @Override
                    public String apply(String s) throws RuntimeException {
                        return s;
                    }
                }
        );
    }
}
