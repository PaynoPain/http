package com.touchiteasy.http.cache;

public interface Policy {
    /**
     * Has the datum expired?
     *
     * @return true when expired.
     */
    boolean hasExpired();

    /**
     * Notify about an updated datum.
     */
    void cacheMiss();

    /**
     * Notify about the usage of the cached datum.
     */
    void cacheHit();
}
