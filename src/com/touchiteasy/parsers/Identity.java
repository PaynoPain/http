package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;

public class Identity<T> implements Function<T, T> {
    @Override
    public T apply(T t) throws RuntimeException {
        return t;
    }
}