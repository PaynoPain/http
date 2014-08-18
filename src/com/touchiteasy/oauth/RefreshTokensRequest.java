package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.RequestWithContext;

public class RefreshTokensRequest extends RequestWithContext {
    public RefreshTokensRequest(Tokens tokens, Request base) {
        super(
                base,
                new AdditionalParametersContext(new LiteralStringsMap(
                        "grant_type", "refresh_token",
                        "refresh_token", tokens.getRefresh()
                ))
        );
    }
}
