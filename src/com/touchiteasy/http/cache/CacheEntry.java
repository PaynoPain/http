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
}