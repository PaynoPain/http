package com.touchiteasy.http.queue;

import java.util.LinkedList;

public class FifoStorageInMemory<T> implements FifoStorage<T> {
    private LinkedList<T> elements = new LinkedList<T>();

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public void add(T element) {
        elements.add(element);
    }

    @Override
    public T getFirst() {
        return elements.element();
    }

    @Override
    public void removeFirst() {
        elements.remove();
    }
}
