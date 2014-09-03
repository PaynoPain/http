package com.touchiteasy.http.queue;

import java.util.List;

public class FlushableCollection implements Flushable {
    private final List<Flushable> elements;

    public FlushableCollection(List<Flushable> elements) {
        this.elements = elements;
    }

    @Override
    public boolean canFlush() {
        return !elements.isEmpty() && elements.get(0).canFlush();
    }

    @Override
    public void flush() {

    }
}
