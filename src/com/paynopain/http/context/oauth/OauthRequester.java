package com.paynopain.http.context.oauth;

import com.paynopain.http.*;

public class OauthRequester implements ResourceRequester {
    private final ResourceRequester oauthRequester;
    private final ResourceRequester resourceRequester;
    private final TokensStorage tokens;
    private final Client client;
    private final User user;
    private final String tokensURL;

    public OauthRequester(ResourceRequester resourceRequester, ResourceRequester oauthRequester, TokensStorage storage, Client client, User user, String tokensURL) {
        this.resourceRequester = resourceRequester;
        this.oauthRequester = oauthRequester;
        this.tokens = storage;
        this.client = client;
        this.user = user;
        this.tokensURL = tokensURL;
    }

    @Override
    public Response run(Request request) {
        if (tokens.isEmpty()) {
            return loginRequest(request);
        }
        Response resourceResponse = resourceRequester.run(getResourceRequest(request));

        if (isAuthError(resourceResponse)) {
            Response refreshResponse = runRefreshRequest();
            //400
            throwIfResponseHaveAInternalError(refreshResponse);

            if (isAuthError(refreshResponse)) {
                return loginRequest(request);
            }
            setTokens(refreshResponse);

            Response finalResponse = resourceRequester.run(getResourceRequest(request));
            //401
            throwIfResponseHaveAuthError(finalResponse);
            return finalResponse;
        }
        return resourceResponse;

    }

    public boolean canLogin(){
        return getResponseFromLoginRequest().getStatusCode() == 200;
    }

    private Response loginRequest(Request request) {
        Response loginResponse = getResponseFromLoginRequest();

        //400 y 401
        throwIfResponseHaveAInternalError(loginResponse);
        throwIfResponseHaveAuthError(loginResponse);

        setTokens(loginResponse);

        Response finalResponse = resourceRequester.run(getResourceRequest(request));
        //401
        throwIfResponseHaveAuthError(finalResponse);
        return finalResponse;
    }
    private Request getResourceRequest(Request request) {
        return new ResourceRequest(tokens.get(), request);
    }

    private Response runRefreshRequest() {
        Request refreshRequest = new RefreshTokensRequest(tokens.get(), new ClientContextRequest(client, new BaseRequest(tokensURL)));
        return oauthRequester.run(refreshRequest);
    }

    private void setTokens(Response loginResponse) {
        tokens.set(new JsonTokens(loginResponse));
    }

    private Response getResponseFromLoginRequest() {
        Request req = new LoginRequest(user, new ClientContextRequest(client, new BaseRequest(tokensURL)));
        return oauthRequester.run(req);
    }

    private void throwIfResponseHaveAuthError(Response res) {
        if (isAuthError(res)) {
            throw new AuthenticationError("Wrong user or password" + new IdentifiableResponse(res).toString());
        }
    }

    private void throwIfResponseHaveAInternalError(Response res) {
        if (isInternalError(res)) {
            throw new InternalError("Internal Error: " + new IdentifiableResponse(res).toString());
        }
    }

    private boolean isInternalError(Response res) {
        return res.getStatusCode() == 400;
    }

    private boolean isAuthError(Response res) {
        return res.getStatusCode() == 401;
    }
}
