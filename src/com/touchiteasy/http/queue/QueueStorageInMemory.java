package com.touchiteasy.http.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueStorageInMemory<T> implements QueueStorage<T> {
    private Queue<T> elements = new ConcurrentLinkedQueue<T>();

    @Override
    public void add(T element) {
        elements.add(element);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public T peek() {
        return elements.element();
    }

    @Override
    public void dequeue() {
        elements.remove();
    }
}
