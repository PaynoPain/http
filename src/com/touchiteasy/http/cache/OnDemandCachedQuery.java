package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.actions.RequestComposer;
import com.touchiteasy.http.actions.RequesterAction;
import com.touchiteasy.http.actions.ResponseInterpreter;
import com.touchiteasy.http.validation.ResponseValidatingRequester;
import com.touchiteasy.http.validation.ResponseValidator;

import java.util.Date;

public class OnDemandCachedQuery<Input, Output> extends RequesterAction<Input, Output> {
    public OnDemandCachedQuery(
            final ResourceRequester requester, final MapStorage<Request, CacheEntry> storage,
            final ResponseValidator cacheValidator,
            final RequestComposer<Input> inputConverter, final ResponseInterpreter<Output> outputConverter,
            final Factory<Date> timeGateway,
            final Long cacheDurationInMilliseconds, final Long timeToRefreshAfterCacheExpirationInMilliseconds) {
        super(
                new OnDemandCacheRequester(
                        new ResponseValidatingRequester(
                                requester,
                                cacheValidator
                        ),
                        storage,
                        timeGateway,
                        cacheDurationInMilliseconds,
                        timeToRefreshAfterCacheExpirationInMilliseconds
                ),
                inputConverter,
                outputConverter
        );
    }
}
