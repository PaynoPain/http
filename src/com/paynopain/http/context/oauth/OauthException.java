package com.paynopain.http.context.oauth;

public class OauthException extends RuntimeException {
    OauthException(String message) {
        super(message);
    }
    OauthException(Throwable cause){
        super(cause);
    }
}
