package com.paynopain.http.context;

import com.paynopain.http.Request;

import java.util.Map;

public class ContextRequest implements Request {
    private final Request baseRequest;
    private final Context context;

    public ContextRequest(Request baseRequest, Context context) {
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
