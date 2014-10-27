package com.touchiteasy.http.context.oauth;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.ContextRequest;

public class ClientContextRequest extends ContextRequest {
    public ClientContextRequest(Client client, Request base){
        super(
                base,
                new AdditionalParametersContext(new LiteralHashMap<String, String>(
                        "client_id", client.id,
                        "client_secret", client.secret
                ))
        );
    }
}
