package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.Response;

public class CacheEntry {
    public final Request tag;
    public final Policy policy;
    public final Response datum;

    public CacheEntry(Request tag, Policy policy, Response datum) {
        this.tag = tag;
        this.policy = policy;
        this.datum = datum;
    }
}
