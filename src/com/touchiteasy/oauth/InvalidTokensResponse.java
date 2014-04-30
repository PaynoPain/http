package com.touchiteasy.oauth;

public class InvalidTokensResponse extends RuntimeException {
    InvalidTokensResponse(String message) {
        super(message);
    }
    InvalidTokensResponse(Throwable cause){
        super(cause);
    }
}
