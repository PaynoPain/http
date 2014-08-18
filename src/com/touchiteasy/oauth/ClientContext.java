package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.AdditionalParametersContext;
import com.touchiteasy.http.context.RequestWithContext;

public class ClientContext extends RequestWithContext {
    public ClientContext(Client client, Request base){
        super(
                base,
                new AdditionalParametersContext(new LiteralStringsMap(
                        "client_id", client.id,
                        "client_secret", client.secret
                ))
        );
    }
}
