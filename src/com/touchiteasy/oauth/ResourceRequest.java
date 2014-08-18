package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.RequestWithContextParameters;

public class ResourceRequest extends RequestWithContextParameters {
    public ResourceRequest(Tokens tokens, Request base) {
        super(
                base,
                new LiteralStringsMap(
                        "access_token", tokens.getAccess()
                )
        );
    }
}
