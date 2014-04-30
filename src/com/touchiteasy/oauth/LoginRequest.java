package com.touchiteasy.oauth;

import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends RequestWithContext {
    private static Map<String, String> getLoginParameters(String username, String password){
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.put("grant_type", "password");
        paramsToSend.put("username", username);
        paramsToSend.put("password", password);
        return paramsToSend;
    }

    public LoginRequest(String username, String password, Request base){
        super(base, getLoginParameters(username, password));
    }
}
