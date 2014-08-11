package com.touchiteasy.http.queue;

public class QueueStorageInMemoryTest extends QueueStorageContract {
    @Override
    public QueueStorage createInstance() {
        return new QueueStorageInMemory();
    }
}
