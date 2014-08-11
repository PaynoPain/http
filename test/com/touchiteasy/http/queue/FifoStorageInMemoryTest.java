package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;

public class FifoStorageInMemoryTest extends QueueStorageContract {
    @Override
    public FifoStorage<Request> createInstance() {
        return new FifoStorageInMemory<Request>();
    }
}
