package com.touchiteasy.commons;

public class MutableFactory<T> implements Factory<T> {
    private T value;

    public MutableFactory(T initialValue){
        set(initialValue);
    }

    public void set(T value){
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
