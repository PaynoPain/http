package com.touchiteasy.http.cache;

public class LayeredMapStorage<K, V> implements MapStorage<K, V> {
    private final MapStorage<K, V> fast;
    private final MapStorage<K, V> persistent;

    public LayeredMapStorage(MapStorage<K, V> fast, MapStorage<K, V> persistent) {
        this.fast = fast;
        this.persistent = persistent;
    }

    @Override
    public boolean contains(K k) {
        return persistent.contains(k);
    }

    @Override
    public V read(K k) {
        if (fast.contains(k)) {
            return fast.read(k);
        } else {
            final V v = persistent.read(k);
            fast.write(k, v);
            return v;
        }
    }

    @Override
    public void write(K k, V v) {
        persistent.write(k, v);
        fast.write(k, v);
    }
}
