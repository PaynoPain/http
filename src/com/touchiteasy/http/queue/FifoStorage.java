package com.touchiteasy.http.queue;

public interface FifoStorage<T> {
    public boolean isEmpty();
    public void add(T element);
    public T getFirst();
    public void removeFirst();
}