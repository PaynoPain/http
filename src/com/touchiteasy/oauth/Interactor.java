package com.touchiteasy.oauth;

import com.touchiteasy.http.*;

public class Interactor implements ResourceRequester{
    private final ResourceRequester requester;
    private final TokensStorage tokens;
    private final Client client;
    private final User user;
    private final String tokensURL;

    public Interactor(ResourceRequester requester, TokensStorage storage, Client client, User user, String tokensURL) {
        this.requester = requester;
        this.tokens = storage;
        this.client = client;
        this.user = user;
        this.tokensURL = tokensURL;
    }

    @Override
    public Response run(Request request){
        if(tokens.isEmpty()){
            Response res = getResponseLoginResponse();
            throwIfResponseIsNotSuccess(res);

            tokens.set(new JsonTokens(res));

            Request req = new ResourceRequest(tokens.get(), request);

            Response finalResponse = requester.run(req);

            if (finalResponse.getStatusCode()==401){
                throw new AuthenticationError("Exception to Authenticate");
            }
            return finalResponse;
        }else{
            Request req = new ResourceRequest(tokens.get(), request);
            return requester.run(req);
        }
    }

    private Response getResponseLoginResponse() {
        Request req = new LoginRequest(user, new ClientContext(client,new BaseRequest(tokensURL)));
        return requester.run(req);
    }

    private void throwIfResponseIsNotSuccess(Response res) {
        if(res.getStatusCode()==400){
            throw new InternalError("Internal Error");
        }
        if(res.getStatusCode()==401){
            throw new AuthenticationError("Wrong user or password");
        }
    }
}
