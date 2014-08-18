package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

public class RequesterWithContext implements ResourceRequester {
    private final ResourceRequester base;
    private final ContextAdder contextAdder;

    public RequesterWithContext(ResourceRequester base, ContextAdder contextAdder) {
        this.base = base;
        this.contextAdder = contextAdder;
    }

    @Override
    public Response run(Request request) {
        return this.base.run(new RequestWithContext(request, contextAdder));
    }
}
