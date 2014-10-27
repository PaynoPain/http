package com.touchiteasy.http.context.oauth;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.ContextRequest;

public class LoginRequest extends ContextRequest {
    public LoginRequest(User user, Request base){
        super(
                base,
                new AdditionalParametersContext(new LiteralHashMap<String, String>(
                        "grant_type", "password",
                        "username", user.name,
                        "password", user.password
                ))
        );
    }
}
