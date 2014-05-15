package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

public class CacheRequester implements ResourceRequester {
    private final ResourceRequester requester;
    private final PolicyFactory factory;

    public CacheRequester(ResourceRequester resourceRequester, PolicyFactory policyFactory) {
        factory = policyFactory;
        requester = resourceRequester;
    }

    private Policy get(Request r){
        Policy p;

        if (hasPolicy(r)) {
            p = getPolicy(r);
        } else {
            p = this.factory.construct(r);
        }

        return p;
    }

    private Policy getPolicy(Request r) {
        //TODO
        return null;
    }

    private boolean hasPolicy(Request r) {
        //TODO
        return false;
    }

    @Override
    public Response run(Request r) {
        return requester.run(r);
    }

    public void invalidate(){
        //TODO
    }

    public void refresh(){
        //TODO
    }
}
