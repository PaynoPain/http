package com.touchiteasy.oauth;

public class InternalError extends OauthException {
    InternalError(String message) {
        super(message);
    }
    InternalError(Throwable cause){
        super(cause);
    }
}
