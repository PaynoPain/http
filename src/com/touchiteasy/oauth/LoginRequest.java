package com.touchiteasy.oauth;

import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.Request;

public class LoginRequest extends RequestWithContext {
    public LoginRequest(String username, String password, Request base){
        super(
                base,
                new LiteralStringsMap(
                        "grant_type", "password",
                        "username", username,
                        "password", password
                )
        );
    }
}
