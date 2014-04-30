package com.touchiteasy.oauth;

import com.touchiteasy.http.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTokens implements Tokens {
    private static final int SUCCESS = 200;

    private final String access, refresh;

    public JsonTokens(Response response) {
        if (response == null){
            throw new NullPointerException("Null response!");
        }

        throwIfCodeIsNotSuccess(response);

        try {
            JSONObject json = new JSONObject(response.getBody());
            access = json.getString("access_token");
            refresh = json.getString("refresh_token");
        } catch (JSONException e){
            throw new InvalidTokensResponse(e);
        }
    }

    private void throwIfCodeIsNotSuccess(Response response) throws InvalidTokensResponse {
        if(response.getStatusCode() != SUCCESS){
            throw new InvalidTokensResponse("The response code is not 200!");
        }
    }

    @Override
    public String getAccess() {
        return access;
    }

    @Override
    public String getRefresh() {
        return refresh;
    }
}
