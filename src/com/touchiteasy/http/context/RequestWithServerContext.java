package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;

import java.util.Map;

public class RequestWithServerContext implements Request {
    private final RequestWithContext requestWithContext;

    public RequestWithServerContext(final Request base, final Server server){
        requestWithContext = new RequestWithContext(
                base,
                new ContextAdder() {
                    @Override
                    public String getResourceWithContext(String baseResource) {
                        return server.getBaseUri() + "/" + baseResource;
                    }
                }
        );
    }

    @Override
    public String getResource() {
        return requestWithContext.getResource();
    }

    @Override
    public Map<String, String> getParameters() {
        return requestWithContext.getParameters();
    }
}