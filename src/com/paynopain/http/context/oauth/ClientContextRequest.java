package com.paynopain.http.context.oauth;

import com.paynopain.commons.LiteralHashMap;
import com.paynopain.http.Request;
import com.paynopain.http.context.AdditionalParametersContext;
import com.paynopain.http.context.ContextRequest;

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
