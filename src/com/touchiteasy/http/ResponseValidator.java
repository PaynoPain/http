package com.touchiteasy.http;

public abstract class ResponseValidator {
    public static class InvalidResponseException extends RuntimeException {
        public InvalidResponseException(String message){
            super(message);
        }
    }

    protected abstract boolean isValid(Response response);
    protected abstract String getCauseDescription();

    public void validate(Response response) {
        if (!isValid(response))
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    getCauseDescription(),
                    new IdentifiableResponse(response).toString()
            ));
    }
}
