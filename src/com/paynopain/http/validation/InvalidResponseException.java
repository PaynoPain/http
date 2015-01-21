package com.paynopain.http.validation;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException(String message){
        super(message);
    }
}
