package com.touchiteasy.oauth;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

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
    public Response run(Request request) {
        Request req = new ClientContext(
                client,
                new LoginRequest(
                        user,
                        request
                )
        );

        return requester.run(req);
    }
}
