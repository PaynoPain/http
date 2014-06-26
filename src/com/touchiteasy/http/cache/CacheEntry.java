package com.touchiteasy.http.cache;

import com.touchiteasy.http.Response;

import java.util.Date;

public class CacheEntry {
    public final Date expiration;
    public final Date deadline;
    public final Response response;

    public CacheEntry(Response response, Date expiration, Date deadline) {
        this.response = response;
        this.expiration = expiration;
        this.deadline = deadline;
    }

    public int hashCode(){
        return this.expiration.hashCode() + this.response.hashCode();
    }

    public boolean equals(Object other){
        if (!(other instanceof CacheEntry)) return false;

        CacheEntry entry = (CacheEntry) other;
        return this.expiration.equals(entry.expiration) &&
                this.response.getStatusCode() == entry.response.getStatusCode() &&
                this.response.getBody().equals(entry.response.getBody());
    }
}