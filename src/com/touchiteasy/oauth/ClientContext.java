package com.touchiteasy.oauth;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class ClientContext implements Request {
    private final Request base;
    private final Map<String, String> parametersWithClient;

    public ClientContext(Request base, String id, String secret){
        this.base = base;
        this.parametersWithClient = addClientTo(base.getParameters(), id, secret);
    }

    private static Map<String, String> addClientTo(Map<String, String> parameters, String id, String secret) {
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.putAll(parameters);
        paramsToSend.put("client_id", id);
        paramsToSend.put("client_secret", secret);

        return paramsToSend;
    }

    @Override
    public String getResource() {
        return this.base.getResource();
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parametersWithClient;
    }
}
