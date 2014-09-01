package com.touchiteasy.http.validation;

import com.touchiteasy.http.IdentifiableResponse;
import com.touchiteasy.http.Response;

public abstract class ResponseValidator {
    public static class InvalidResponseException extends RuntimeException {
        public InvalidResponseException(String message){
            super(message);
        }
    }

    protected abstract boolean isValid(Response response);
    protected abstract String getCauseDescription();

    public void validate(Response response) throws InvalidResponseException {
        if (!isValid(response))
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    getCauseDescription(),
                    new IdentifiableResponse(response).toString()
            ));
    }
}
