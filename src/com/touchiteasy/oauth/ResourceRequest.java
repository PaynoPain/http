package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.RequestWithContext;

public class ResourceRequest extends RequestWithContext {
    public ResourceRequest(Tokens tokens, Request base) {
        super(
                base,
                new LiteralStringsMap(
                        "access_token", tokens.getAccess()
                )
        );
    }
}
