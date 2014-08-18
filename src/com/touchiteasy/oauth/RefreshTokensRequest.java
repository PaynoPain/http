package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.RequestWithContextParameters;

public class RefreshTokensRequest extends RequestWithContextParameters {
    public RefreshTokensRequest(Tokens tokens, Request base) {
        super(
                base,
                new LiteralStringsMap(
                        "grant_type", "refresh_token",
                        "refresh_token", tokens.getRefresh()
                )
        );
    }
}
