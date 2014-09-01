package com.touchiteasy.http.queue;

public interface QueueStorage<T> {
    public boolean isEmpty();
    public T peek();
    public void dequeue();
}