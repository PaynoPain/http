package com.touchiteasy.http;

public class ResponseValidatingRequester implements ResourceRequester {
    public static interface ResponseValidator {
        public boolean isValid(Response response);
        public String getCauseDescription();
    }

    public static class InvalidResponseException extends RuntimeException {
        public InvalidResponseException(String message){
            super(message);
        }
    }

    private final ResourceRequester baseRequester;
    private final ResponseValidator validator;

    public ResponseValidatingRequester(ResourceRequester baseRequester, ResponseValidator validator) {
        this.baseRequester = baseRequester;
        this.validator = validator;
    }

    @Override
    public Response run(Request request) {
        Response response = baseRequester.run(request);

        if (!validator.isValid(response))
            throw new InvalidResponseException(this.validator.getCauseDescription());

        return response;
    }
}
