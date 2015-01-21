package com.paynopain.http.cache;

public interface MapStorage<K, V> {
    public boolean contains(K k);
    public V read(K k);
    public void write(K k, V v);
}
