package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.RequestWithContext;

public class LoginRequest extends RequestWithContext {
    public LoginRequest(User user, Request base){
        super(
                base,
                new LiteralStringsMap(
                        "grant_type", "password",
                        "username", user.name,
                        "password", user.password
                )
        );
    }
}
