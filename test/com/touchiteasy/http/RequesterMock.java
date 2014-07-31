package com.touchiteasy.http;

import java.util.ArrayList;
import java.util.List;

public class RequesterMock implements ResourceRequester {
    /**
     * The list of responses to respond.
     * The first response will be responded with the first call to run(),
     * the second response with the second call, and so on...
     */
    public List<Response> responses = new ArrayList<Response>();

    /**
     * The list of requests received.
     * The first request will be the received request with the first call to run(),
     * the second request with the second call, and so on...
     */
    public List<Request> requests = new ArrayList<Request>();

    private int responseIndex = 0;

    /**
     * Register the request received and respond the corresponding configured response in the responses list.
     * @param request The request to register.
     * @return The n response configured in the responses list.
     */
    @Override
    public Response run(Request request) {
        requests.add(request);

        if (responses.size() <= responseIndex)
            throw new RuntimeException("There aren't more responses configured!");

        Response r = responses.get(responseIndex);
        responseIndex++;
        return r;
    }
}