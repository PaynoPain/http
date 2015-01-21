package com.paynopain.http.context;

public class ServerContext extends Context {
    private final Server server;

    public ServerContext(final Server server){
        this.server = server;
    }

    @Override
    public String getResourceWithContext(String baseResource) {
        return server.getBaseUri() + "/" + baseResource;
    }
}