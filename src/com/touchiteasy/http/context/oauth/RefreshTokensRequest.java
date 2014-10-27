package com.touchiteasy.http.context.oauth;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.ContextRequest;

public class RefreshTokensRequest extends ContextRequest {
    public RefreshTokensRequest(Tokens tokens, Request base) {
        super(
                base,
                new AdditionalParametersContext(new LiteralHashMap<String, String>(
                        "grant_type", "refresh_token",
                        "refresh_token", tokens.getRefresh()
                ))
        );
    }
}
