package com.touchiteasy.http;

import com.touchiteasy.commons.Factory;

import java.util.ArrayList;
import java.util.List;

public class ServerMock implements ResourceRequester {
    public void addResponse(final Response response){
        responseFactories.add(new Factory<Response>() {
            @Override
            public Response get() {
                return response;
            }
        });
    }

    public void addResponseFactory(Factory<Response> responseFactory){
        responseFactories.add(responseFactory);
    }

    private List<Factory<Response>> responseFactories = new ArrayList<Factory<Response>>();
    public List<Request> requests = new ArrayList<Request>();

    private int responseIndex = 0;

    @Override
    public Response run(Request request) {
        requests.add(request);

        if (responseFactories.size() <= responseIndex)
            throw new RuntimeException("There aren't more responses configured!");

        Response r = responseFactories.get(responseIndex).get();
        responseIndex++;
        return r;
    }
}