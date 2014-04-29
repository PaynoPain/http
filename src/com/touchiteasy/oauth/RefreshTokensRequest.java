package com.touchiteasy.oauth;

import com.touchiteasy.http.BaseRequest;

public class RefreshTokensRequest extends BaseRequest{
    public RefreshTokensRequest(String resource, Tokens tokens) {
        super(
                resource,
                "grant_type", "refresh_token",
                "refresh_token", tokens.refresh
        );
    }
}
