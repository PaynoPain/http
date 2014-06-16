package com.touchiteasy.http;

import java.util.ArrayList;
import java.util.List;

public class RequesterMock implements ResourceRequester {
    public List<Response> responses = new ArrayList<Response>();
    public List<Request> requests = new ArrayList<Request>();

    private int responseIndex = 0;

    @Override
    public Response run(Request request) {
        requests.add(request);

        if (responses.size() <= responseIndex)
            throw new RuntimeException("There isn't more responses configured!");

        Response r = responses.get(responseIndex);
        responseIndex++;
        return r;
    }
}