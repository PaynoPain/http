package com.touchiteasy.http.context;

public class ServerContext extends ContextAdder {
    private final Server server;

    public ServerContext(final Server server){
        this.server = server;
    }

    @Override
    public String getResourceWithContext(String baseResource) {
        return server.getBaseUri() + "/" + baseResource;
    }
}