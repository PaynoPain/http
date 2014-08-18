package com.touchiteasy.http;

import java.util.Map;

public class RequesterWithContextParameters implements ResourceRequester {
    private ResourceRequester base;
    private Map<String, String> additionalParameters;

    public RequesterWithContextParameters(ResourceRequester base, Map<String, String> additionalParameters) {
        this.base = base;
        this.additionalParameters = additionalParameters;
    }

    @Override
    public Response run(Request request) {
        return this.base.run(new RequestWithContextParameters(request, this.additionalParameters));
    }
}