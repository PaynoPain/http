package com.touchiteasy.http.context;

public class PrependToResourceContext extends Context {
    private final String toPrepend;

    public PrependToResourceContext(final String toPrepend){
        this.toPrepend = toPrepend;
    }

    @Override
    public String getResourceWithContext(String baseResource) {
        return toPrepend + baseResource;
    }
}
