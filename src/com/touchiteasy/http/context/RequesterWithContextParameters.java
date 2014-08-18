package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

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
