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
            Response finalResponse = loginRequest(request);
            return finalResponse;
        }else{
            Request req = new ResourceRequest(tokens.get(), request);
            Response res = requester.run(req);

            if(res.getStatusCode()==401){
                Request refreshRequest = new RefreshTokensRequest(tokens.get(), new ClientContext(client, new BaseRequest(tokensURL)));
                Response refreshResponse = requester.run(refreshRequest);
                //400
                throwIfResponseHaveAInternalError(refreshResponse);

                if(refreshResponse.getStatusCode()==401){
                    Response finalResponse = loginRequest(request);
                    return finalResponse;
                }
                tokens.set(new JsonTokens(refreshResponse));

                Request requestFromRefresh = new ResourceRequest(tokens.get(), request);

                Response finalResponse = requester.run(requestFromRefresh);
                //401
                throwIfResponseHaveAuthError(finalResponse);
                return finalResponse;
            }
            return res;
        }
    }

    private Response loginRequest(Request request) {
        Response res = getResponseLoginResponse();

        //400 y 401
        throwIfResponseHaveAInternalError(res);
        throwIfResponseHaveAuthError(res);

        tokens.set(new JsonTokens(res));

        Request req = new ResourceRequest(tokens.get(), request);

        Response finalResponse = requester.run(req);
        //401
        throwIfResponseHaveAuthError(finalResponse);
        return finalResponse;
    }

    private Response getResponseLoginResponse() {
        Request req = new LoginRequest(user, new ClientContext(client,new BaseRequest(tokensURL)));
        return requester.run(req);
    }

    private void throwIfResponseHaveAInternalError(Response res) {
        if(res.getStatusCode()==400){
            throw new InternalError("Internal Error");
        }
    }
    private void throwIfResponseHaveAuthError(Response res){
        if(res.getStatusCode()==401){
            throw new AuthenticationError("Wrong user or password");
        }
    }
}
