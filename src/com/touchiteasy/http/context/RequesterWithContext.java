package com.touchiteasy.http.context;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

public class RequesterWithContext implements ResourceRequester {
    private final ResourceRequester base;
    private final Context context;

    public RequesterWithContext(ResourceRequester base, Context context) {
        this.base = base;
        this.context = context;
    }

    @Override
    public Response run(Request request) {
        return this.base.run(new RequestWithContext(request, context));
    }
}
