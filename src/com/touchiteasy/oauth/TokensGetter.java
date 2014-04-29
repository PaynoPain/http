package com.touchiteasy.oauth;

import com.touchiteasy.http.*;

import java.io.IOException;

public class TokensGetter {
    private final ResourceRequester requester;
    private final Request request;
    private final TokensParser parser;

    public TokensGetter(Request request, ResourceRequester requester, TokensParser parser) {
        this.request = request;
        this.requester = requester;
        this.parser = parser;
    }

    public Tokens execute() throws IOException {
        try {    
            Response response = this.requester.run(request);
            return this.parser.constructFrom(response);
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }
}

