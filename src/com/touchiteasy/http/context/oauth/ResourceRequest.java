package com.touchiteasy.http.context.oauth;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.ContextRequest;

public class ResourceRequest extends ContextRequest {
    public ResourceRequest(Tokens tokens, Request base) {
        super(
                base,
                new AdditionalParametersContext(new LiteralHashMap<String, String>(
                        "access_token", tokens.getAccess()
                ))
        );
    }
}
