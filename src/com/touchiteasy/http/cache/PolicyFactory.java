package com.touchiteasy.http.cache;

import com.touchiteasy.http.Request;

public interface PolicyFactory {
    /**
     * Construct a cache policy for the request.
     *
     * @param request
     * @return
     */
    Policy construct(Request request);
}
