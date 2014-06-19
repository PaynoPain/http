package com.touchiteasy.oauth;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.Request;

public class ClientContext extends RequestWithContext {
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
