package com.paynopain.http.queue;

public interface Flushable {
    public boolean canFlush();
    public void flush();
}
