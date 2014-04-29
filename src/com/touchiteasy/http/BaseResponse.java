package com.touchiteasy.http;

public class BaseResponse implements Response {
    private final int statusCode;
    private final String body;

    public BaseResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getBody() {
        return body;
    }
}
