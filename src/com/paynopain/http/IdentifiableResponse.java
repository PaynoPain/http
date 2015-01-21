package com.paynopain.http;

public class IdentifiableResponse implements Response {
    private final Response base;

    public IdentifiableResponse(Response base){
        this.base = base;
    }

    @Override
    public int getStatusCode() {
        return base.getStatusCode();
    }

    @Override
    public String getBody() {
        return base.getBody();
    }

    @Override
    public int hashCode() {
        return base.getStatusCode() + base.getBody().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Response &&
                this.base.getStatusCode() == ((Response) obj).getStatusCode() &&
                this.base.getBody().equals(((Response) obj).getBody());
    }

    @Override
    public String toString() {
        return "StatusCode: " + base.getStatusCode() +
                "\nBody: " + base.getBody();
    }
}
