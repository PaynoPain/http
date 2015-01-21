package com.paynopain.http.context.oauth;

import com.paynopain.commons.LiteralHashMap;
import com.paynopain.http.Request;
import com.paynopain.http.context.AdditionalParametersContext;
import com.paynopain.http.context.ContextRequest;

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
