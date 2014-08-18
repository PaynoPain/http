package com.touchiteasy.http;

public class ResponseValidatingRequester implements ResourceRequester {
    private final ResourceRequester baseRequester;
    private final ResponseValidator validator;

    public ResponseValidatingRequester(ResourceRequester baseRequester, ResponseValidator validator) {
        this.baseRequester = baseRequester;
        this.validator = validator;
    }

    @Override
    public Response run(Request request) {
        Response response = baseRequester.run(request);
        validator.validate(response);
        return response;
    }
}
