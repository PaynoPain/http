package com.touchiteasy.http.context.oauth;

public class InternalError extends OauthException {
    InternalError(String message) {
        super(message);
    }
    InternalError(Throwable cause){
        super(cause);
    }
}
