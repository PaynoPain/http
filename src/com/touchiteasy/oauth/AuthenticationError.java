package com.touchiteasy.oauth;

public class AuthenticationError extends RuntimeException {
    AuthenticationError(String message) {
        super(message);
    }
    AuthenticationError(Throwable cause){
        super(cause);
    }
}
