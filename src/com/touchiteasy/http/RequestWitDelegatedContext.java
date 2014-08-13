package com.touchiteasy.http;

import java.util.Map;

public class RequestWitDelegatedContext implements Request {
    public static abstract class ContextAdder {
        public String getResourceWithContext(String baseResource){
            return baseResource;
        }
        public Map<String, String> getParametersWithContext(Map<String, String> baseParameters){
            return baseParameters;
        }
    }

    private final Request baseRequest;
    private final ContextAdder contextAdder;

    public RequestWitDelegatedContext(Request baseRequest, ContextAdder contextAdder) {
        this.baseRequest = baseRequest;
        this.contextAdder = contextAdder;
    }

    @Override
    public String getResource() {
        return contextAdder.getResourceWithContext(baseRequest.getResource());
    }

    @Override
    public Map<String, String> getParameters() {
        return contextAdder.getParametersWithContext(baseRequest.getParameters());
    }
}
