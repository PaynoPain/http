package com.touchiteasy.oauth;

import com.touchiteasy.http.*;
import java.io.IOException;

public class Interactor implements ResourceRequester{
    private final ResourceRequester requester;
    private final TokensStorage tokens;
    private final Client client;
    private final User user;

    public Interactor(ResourceRequester requester, TokensStorage storage, Client client, User user) {
        this.requester = requester;
        this.tokens = storage;
        this.client = client;
        this.user = user;
    }

    @Override
    public Response run(Request request) throws IOException {
        if (tokens.isEmpty()) {
            tokens.set(new JsonTokens(getLoginResponse(request)));
        }
        Response finalResponse = runRequest(request);

        if(finalResponse.getStatusCode()==401){
            tokens.set(new JsonTokens(getRefreshResponse(request)));
            finalResponse = runRequest(request);
        }
        if(finalResponse.getStatusCode()==400){
            throw new IOException();
        }

        return finalResponse;
    }

    private Response runRequest(Request request) throws IOException {
        Request req = new ResourceRequest(tokens.get(), request);
        return requester.run(req);
    }

    private Response getLoginResponse(Request request) throws IOException {
        Request req = new ClientContext(
                client,
                new LoginRequest(
                        user,
                        request
                )
        );

        return requester.run(req);
    }
    private Response getRefreshResponse(Request request) throws IOException {
        Request req = new ClientContext(
                client,
                new RefreshTokensRequest(
                        tokens.get(),
                        request
                )
        );

        return requester.run(req);
    }
}
