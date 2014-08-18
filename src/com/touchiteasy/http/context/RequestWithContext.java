package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;

import java.util.Map;

public class RequestWithContext implements Request {
    private final Request baseRequest;
    private final ContextAdder contextAdder;

    public RequestWithContext(Request baseRequest, ContextAdder contextAdder) {
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
