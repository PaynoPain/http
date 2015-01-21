package com.paynopain.http.context.oauth;

import com.paynopain.commons.LiteralHashMap;
import com.paynopain.http.Request;
import com.paynopain.http.context.AdditionalParametersContext;
import com.paynopain.http.context.ContextRequest;

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
