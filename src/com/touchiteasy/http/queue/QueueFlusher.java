package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;

public class QueueFlusher {
    private final ResourceRequester requester;
    private final QueueStorage<Request> queue;

    public QueueFlusher(ResourceRequester requester, QueueStorage<Request> queue) {
        this.requester = requester;
        this.queue = queue;
    }

    public void flush() {
        try {
            while (!queue.isEmpty()) {
                requester.run(queue.peek());
                queue.dequeue();
            }
        } catch (RuntimeException ignored){}
    }
}
