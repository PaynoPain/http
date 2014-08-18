package com.touchiteasy.http.context;

public class Server {
    public enum Scheme {
        HTTP("http"), HTTPS("https");

        private String id;
        Scheme(String id){
            this.id = id;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    private final String hostname;
    private final Scheme scheme;
    private final Integer port;

    public Server(Scheme scheme, String hostname, Integer port) {
        for (Object o : new Object[]{scheme, hostname, port})
            if (o == null)
                throw new IllegalArgumentException("The arguments are not allowed to be null!");

        this.scheme = scheme;
        this.hostname = hostname;
        this.port = port;
    }

    public String getBaseUri() {
        return String.format(
                "%s://%s:%s",
                this.scheme.toString(), this.hostname, String.valueOf(this.port)
        );
    }
}
