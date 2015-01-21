package com.paynopain.commons;

public interface Function<Input, Output> {
    public Output apply(Input input) throws RuntimeException;
}
