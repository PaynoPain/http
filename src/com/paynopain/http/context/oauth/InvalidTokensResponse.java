package com.paynopain.http.context.oauth;

public class InvalidTokensResponse extends OauthException {
    InvalidTokensResponse(String message) {
        super(message);
    }
    InvalidTokensResponse(Throwable cause){
        super(cause);
    }
}
