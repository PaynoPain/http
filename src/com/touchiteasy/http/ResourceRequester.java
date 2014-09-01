package com.touchiteasy.http;

public interface ResourceRequester {
    /**
     * Run a request to an element able to respond to it.
     * @param request The request to ask.
     * @return The response responded by the element.
     * @throws java.lang.RuntimeException when the request can't be fulfilled.
     */
    public Response run(Request request) throws RuntimeException;
}
