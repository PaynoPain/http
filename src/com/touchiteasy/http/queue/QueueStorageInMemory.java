package com.touchiteasy.http.queue;

import java.util.LinkedList;

public class QueueStorageInMemory<T> implements QueueStorage<T> {
    private LinkedList<T> elements = new LinkedList<T>();

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
