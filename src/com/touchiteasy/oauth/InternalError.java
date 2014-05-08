package com.touchiteasy.oauth;

public class InternalError extends RuntimeException {
    InternalError(String message) {
        super(message);
    }
    InternalError(Throwable cause){
        super(cause);
    }
}
