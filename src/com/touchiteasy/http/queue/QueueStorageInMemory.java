package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;

import java.util.LinkedList;

public class QueueStorageInMemory implements QueueStorage {
    private LinkedList<Request> requests = new LinkedList<Request>();

    @Override
    public boolean isEmpty() {
        return requests.isEmpty();
    }

    @Override
    public void add(Request element) {
        requests.add(element);
    }

    @Override
    public Request getFirst() {
        return requests.element();
    }

    @Override
    public void removeFirst() {
        requests.remove();
    }
}
