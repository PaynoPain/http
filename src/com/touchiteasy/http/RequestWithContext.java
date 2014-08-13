package com.touchiteasy.http;

import java.util.HashMap;
import java.util.Map;

public class RequestWithContext implements Request {
    private final Request requestWithContext;

    public RequestWithContext(Request base, final Map<String, String> additionalParameters){
        final RequestWitDelegatedContext.ContextAdder contextAdder = new RequestWitDelegatedContext.ContextAdder() {
            @Override
            public Map<String, String> getParametersWithContext(Map<String, String> baseParameters) {
                Map<String, String> paramsToSend = new HashMap<String, String>();
                paramsToSend.putAll(baseParameters);
                paramsToSend.putAll(additionalParameters);

                return paramsToSend;
            }
        };
        this.requestWithContext = new RequestWitDelegatedContext(base, contextAdder);
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
