package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.context.RequestWithContextParameters;

public class ClientContext extends RequestWithContextParameters {
    public ClientContext(Client client, Request base){
        super(
                base,
                new LiteralStringsMap(
                        "client_id", client.id,
                        "client_secret", client.secret
                )
        );
    }
}
