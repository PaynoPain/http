package com.paynopain.http.context.oauth;

import com.paynopain.commons.LiteralHashMap;
import com.paynopain.http.Request;
import com.paynopain.http.context.AdditionalParametersContext;
import com.paynopain.http.context.ContextRequest;

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
