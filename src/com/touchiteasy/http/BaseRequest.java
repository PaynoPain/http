package com.touchiteasy.http;

import com.touchiteasy.LiteralStringsMap;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest implements Request {
    private final String resource;
    private final Map<String, String> parameters;

    public BaseRequest(String resource, Map<String, String> parameters) {
        checkField(resource, "resource");
        checkField(parameters, "parameters");

        this.resource = resource;
        this.parameters = parameters;
    }

    public BaseRequest(String resource) {
        this(resource, new HashMap<String, String>());
    }

    private static void checkField(Object obj, String fieldName) {
        if (obj == null){
            throw new IllegalArgumentException("The " + fieldName + " argument is required!");
        }
    }

    @Override
    public String getResource() {
        return this.resource;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public int hashCode() {
        return this.resource.hashCode() + this.parameters.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Request &&
                this.resource.equals(((Request) obj).getResource()) &&
                this.parameters.equals(((Request) obj).getParameters());
    }
}
