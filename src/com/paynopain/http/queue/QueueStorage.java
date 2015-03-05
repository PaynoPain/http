package com.paynopain.http.queue;

public interface QueueStorage<T> {
    public boolean isEmpty();
    public void add(T t);
    public T peek();
    public void dequeue();
    public int size();
}