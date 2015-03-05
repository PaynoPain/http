package com.paynopain.http.queue;

import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;

import java.util.HashSet;
import java.util.Set;

public class QueueFlusher implements Flushable {
    private final ResourceRequester requester;
    private final QueueStorage<Request> queue;

    public QueueFlusher(ResourceRequester requester, QueueStorage<Request> queue) {
        this.requester = requester;
        this.queue = queue;
    }

    @Override
    public synchronized void flush() {
        int triedRequestCount = 0;
        int size = queue.size();
        Set<Request> failedRequests = new HashSet<Request>();
        while (!queue.isEmpty() && triedRequestCount < size) {
            final Request currentRequest = queue.peek();
            boolean runSuccesfull = true;
            try {
                triedRequestCount++;
                requester.run(currentRequest);
            } catch (RuntimeException e){
                runSuccesfull = false;
            }
            queue.dequeue();
            if(!runSuccesfull){
                failedRequests.add(currentRequest);
            }
        }

        for (Request r : failedRequests){
            queue.add(r);
        }
    }

    @Override
    public boolean canFlush() {
        return !queue.isEmpty();
    }
}
