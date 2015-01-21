package com.paynopain.http;

import java.util.Map;

public class IdentifiableRequest implements Request {
    private Request base;

    public IdentifiableRequest(Request base){
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

    @Override
    public String toString() {
        return "Resource: " + base.getResource() +
                "\nParameters: " + mapToString(base.getParameters());
    }

    private String mapToString(Map<String, String> map) {
        return map.toString();
    }
}