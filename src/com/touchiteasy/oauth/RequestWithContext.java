package com.touchiteasy.oauth;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestWithContext implements Request {
    private final Request base;
    private final Map<String, String> parameters;

    public RequestWithContext(Request base, Map<String, String> additionalParameters){
        this.base = base;
        this.parameters = addContext(base, additionalParameters);
    }

    private static Map<String, String> addContext(Request base, Map<String, String> additionalParameters) {
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.putAll(base.getParameters());
        paramsToSend.putAll(additionalParameters);

        return paramsToSend;
    }

    @Override
    public String getResource() {
        return this.base.getResource();
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }
}
