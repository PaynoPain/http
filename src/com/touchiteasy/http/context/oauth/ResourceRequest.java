package com.touchiteasy.http.context.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.ContextRequest;

public class ResourceRequest extends ContextRequest {
    public ResourceRequest(Tokens tokens, Request base) {
        super(
                base,
                new AdditionalParametersContext(new LiteralStringsMap(
                        "access_token", tokens.getAccess()
                ))
        );
    }
}
