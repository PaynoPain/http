package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

import java.util.List;

public class CacheRequester implements ResourceRequester {
    private final ResourceRequester requester;
    private final PolicyFactory factory;

    public List<CacheEntry> cacheEntryList;

    public CacheRequester(ResourceRequester resourceRequester, PolicyFactory policyFactory) {
        factory = policyFactory;
        requester = resourceRequester;
    }

    @Override
    public Response run(Request r) {
        return null;
    }

    public void invalidate(){
        //TODO
    }

    public void refresh(){
        //TODO
    }
}
