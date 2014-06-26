package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

import java.util.Map;

public class HashedRequest implements Request {
    private Request base;

    public HashedRequest(Request base){
        this.base = base;
    }

    @Override
    public String getResource() {
        return this.base.getResource();
    }

    @Override
    public Map<String, String> getParameters() {
        return this.base.getParameters();
    }

    @Override
    public int hashCode() {
        return this.base.getResource().hashCode() + this.base.getParameters().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Request &&
                this.base.getResource().equals(((Request) obj).getResource()) &&
                this.base.getParameters().equals(((Request) obj).getParameters());
    }
}