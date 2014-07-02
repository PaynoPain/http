package com.touchiteasy.oauth;

public class AuthenticationError extends OauthException {
    AuthenticationError(String message) {
        super(message);
    }
    AuthenticationError(Throwable cause){
        super(cause);
    }
}
