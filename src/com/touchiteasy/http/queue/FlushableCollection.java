package com.touchiteasy.http.queue;

import java.util.List;

public class FlushableCollection implements Flushable {
    private final List<Flushable> elements;

    public FlushableCollection(List<Flushable> elements) {
        this.elements = elements;
    }

    @Override
    public boolean canFlush() {
        return !elements.isEmpty() && anyCanFlush();
    }

    private boolean anyCanFlush() {
        boolean canFlush = false;

        for (Flushable flushable : elements){
            canFlush = flushable.canFlush();
            if (canFlush) break;
        }

        return canFlush;
    }

    @Override
    public void flush() {
        throw new RuntimeException("Cant be flushed!");
    }
}
