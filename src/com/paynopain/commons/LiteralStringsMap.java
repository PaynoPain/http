package com.paynopain.commons;

@Deprecated
public class LiteralStringsMap extends LiteralHashMap<String, String> {
    public LiteralStringsMap (String... keyValuePairs){
        super(keyValuePairs);
    }
}
