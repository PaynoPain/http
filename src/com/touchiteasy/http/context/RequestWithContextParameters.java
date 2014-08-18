package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestWithContextParameters implements Request {
    private final Request requestWithContext;

    public RequestWithContextParameters(Request base, final Map<String, String> additionalParameters){
        final ContextAdder contextAdder = new ContextAdder() {
            @Override
            public Map<String, String> getParametersWithContext(Map<String, String> baseParameters) {
                Map<String, String> paramsToSend = new HashMap<String, String>();
                paramsToSend.putAll(baseParameters);
                paramsToSend.putAll(additionalParameters);

                return paramsToSend;
            }
        };
        this.requestWithContext = new RequestWithDelegatedContext(base, contextAdder);
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
