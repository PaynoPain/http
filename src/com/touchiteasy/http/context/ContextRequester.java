package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

public class ContextRequester implements ResourceRequester {
    private final ResourceRequester base;
    private final Context context;

    public ContextRequester(ResourceRequester base, Context context) {
        this.base = base;
        this.context = context;
    }

    @Override
    public Response run(Request request) {
        return this.base.run(new ContextRequest(request, context));
    }
}
