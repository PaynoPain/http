package com.touchiteasy.http.cache;

import com.touchiteasy.http.Response;

import java.util.Date;

public class CacheEntry {
    public final Date expiration;
    public final Response response;

    public CacheEntry(Date expiration, Response response) {
        this.response = response;
        this.expiration = expiration;
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