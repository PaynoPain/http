package com.paynopain.commons;

public class Identity<T> implements Function<T, T> {
    @Override
    public T apply(T t) throws RuntimeException {
        return t;
    }
}