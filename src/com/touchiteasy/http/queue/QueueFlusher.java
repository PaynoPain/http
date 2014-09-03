package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;

public class QueueFlusher implements Flushable {
    private final ResourceRequester requester;
    private final QueueStorage<Request> queue;

    public QueueFlusher(ResourceRequester requester, QueueStorage<Request> queue) {
        this.requester = requester;
        this.queue = queue;
    }

    @Override
    public synchronized void flush() {
        boolean failure = false;

        while (!queue.isEmpty() && !failure) {
            final Request currentRequest = queue.peek();
            try {
                requester.run(currentRequest);
            } catch (RuntimeException e){
                failure = true;
            }
            if (!failure) queue.dequeue();
        }
    }

    @Override
    public boolean canFlush() {
        return !queue.isEmpty();
    }
}
