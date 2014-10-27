package com.touchiteasy.commons;

import java.util.HashMap;

public class LiteralHashMap<K, V> extends HashMap<K, V> {
    public LiteralHashMap (Object... keyValuePairs){
        super();

        if (keyValuePairs.length % 2 != 0){
            throw new IllegalArgumentException("The number of key-value pair arguments should be even!");
        }

        for (int i = 0; i < keyValuePairs.length; i += 2){
            final K key = (K) keyValuePairs[i];
            final V value = (V) keyValuePairs[i + 1];
            this.put(key, value);
        }
    }
}
