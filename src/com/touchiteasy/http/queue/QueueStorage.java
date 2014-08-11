package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;

public interface QueueStorage {
    public boolean isEmpty();
    public void add(Request element);
    public Request getFirst();
    public void removeFirst();
}