package com.touchiteasy.oauth;

import com.touchiteasy.http.BaseRequest;

public class LoginRequest extends BaseRequest {
    public LoginRequest(String resource, String username, String password){
        super(
                resource,
                "grant_type", "password",
                "username", username,
                "password", password
        );
    }
}
