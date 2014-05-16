package com.touchiteasy.http.cache;

public interface PolicyFactory {
    /**
     * Construct a cache policy for the request.
     *
     * @return
     */
    Policy construct();
}
