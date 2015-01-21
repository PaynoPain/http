package com.paynopain.http.context;

import java.util.Map;

public abstract class Context {
    public String getResourceWithContext(String baseResource){
        return baseResource;
    }
    public Map<String, String> getParametersWithContext(Map<String, String> baseParameters){
        return baseParameters;
    }
}