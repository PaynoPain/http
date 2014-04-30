package com.touchiteasy.oauth;

import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.Request;

public class ClientContext extends RequestWithContext {
    public ClientContext(String id, String secret, Request base){
        super(
                base,
                new LiteralStringsMap(
                        "client_id", id,
                        "client_secret", secret
                )
        );
    }
}
