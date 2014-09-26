package com.touchiteasy.http.validation;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException(String message){
        super(message);
    }
}
