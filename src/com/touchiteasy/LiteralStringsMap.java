package com.touchiteasy;

import java.util.HashMap;

public class LiteralStringsMap extends HashMap<String, String> {
    public LiteralStringsMap (String... keyValuePairs){
        super();

        if (keyValuePairs.length % 2 != 0){
            throw new IllegalArgumentException("The number of key-value pair arguments should be even!");
        }

        for (int i = 0; i < keyValuePairs.length; i += 2){
            this.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
    }
}
