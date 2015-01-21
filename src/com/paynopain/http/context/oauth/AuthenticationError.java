package com.paynopain.http.context.oauth;

public class AuthenticationError extends OauthException {
    AuthenticationError(String message) {
        super(message);
    }
    AuthenticationError(Throwable cause){
        super(cause);
    }
}
