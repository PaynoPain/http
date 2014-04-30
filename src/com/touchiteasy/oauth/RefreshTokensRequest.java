package com.touchiteasy.oauth;

import com.touchiteasy.http.Request;

import java.util.HashMap;
import java.util.Map;

public class RefreshTokensRequest extends RequestWithContext{
    private static Map<String, String> getRefreshParameters(Tokens tokens){
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.put("grant_type", "refresh_token");
        paramsToSend.put("refresh_token", tokens.getRefresh());
        return paramsToSend;
    }

    public RefreshTokensRequest(Tokens tokens, Request base) {
        super(base, getRefreshParameters(tokens));
    }
}
