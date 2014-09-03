package com.touchiteasy.http.queue;

public interface Flushable {
    public boolean canFlush();
    public void flush();
}
