package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;

import java.util.Map;

public class RequestWithContext implements Request {
    private final Request baseRequest;
    private final Context context;

    public RequestWithContext(Request baseRequest, Context context) {
        this.baseRequest = baseRequest;
        this.context = context;
    }

    @Override
    public String getResource() {
        return context.getResourceWithContext(baseRequest.getResource());
    }

    @Override
    public Map<String, String> getParameters() {
        return context.getParametersWithContext(baseRequest.getParameters());
    }
}
