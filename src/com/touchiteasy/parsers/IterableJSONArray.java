package com.touchiteasy.parsers;

import org.json.JSONArray;

import java.util.Iterator;

public class IterableJSONArray<T> implements Iterable<T> {
    private final JSONArray array;

    public IterableJSONArray(JSONArray array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length();
            }

            @Override
            public T next() {
                try {
                    final Object o = array.get(i++);
                    final T t = (T) o;
                    return t;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}