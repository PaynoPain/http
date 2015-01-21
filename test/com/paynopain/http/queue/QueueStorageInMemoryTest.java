package com.paynopain.http.queue;

public class QueueStorageInMemoryTest extends QueueStorageContract {
    public QueueStorageInMemory<String> createInstance() {
        return new QueueStorageInMemory<String>();
    }
}
