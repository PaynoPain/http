package com.touchiteasy.oauth;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class ClientContext extends RequestWithContext {
    private static Map<String, String> getClientParameters(String id, String secret){
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.put("client_id", id);
        paramsToSend.put("client_secret", secret);
        return paramsToSend;
    }

    public ClientContext(String id, String secret, Request base){
        super(base, getClientParameters(id, secret));
    }
}
