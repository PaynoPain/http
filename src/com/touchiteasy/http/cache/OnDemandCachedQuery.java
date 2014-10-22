package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.commons.Function;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.actions.RequestComposer;
import com.touchiteasy.http.actions.RequesterAction;
import com.touchiteasy.http.actions.ResponseInterpreter;
import com.touchiteasy.http.validation.ResponseValidatingRequester;
import com.touchiteasy.http.validation.ResponseValidator;

import java.util.Date;

public class OnDemandCachedQuery<Input, Output> implements Function<Input, Output> {
    private final OnDemandCacheRequester onDemandCacheRequester;
    private final RequesterAction<Input, Output> action;
    private final RequestComposer<Input> inputConverter;

    public OnDemandCachedQuery(
            final ResourceRequester requester, final MapStorage<Request, CacheEntry> storage,
            final ResponseValidator cacheValidator,
            final RequestComposer<Input> inputConverter, final ResponseInterpreter<Output> outputConverter,
            final Factory<Date> timeGateway,
            final Long cacheDurationInMilliseconds, final Long timeToRefreshAfterCacheExpirationInMilliseconds) {

        onDemandCacheRequester = new OnDemandCacheRequester(
                new ResponseValidatingRequester(
                        requester,
                        cacheValidator
                ),
                storage,
                timeGateway,
                cacheDurationInMilliseconds,
                timeToRefreshAfterCacheExpirationInMilliseconds
        );

        this.inputConverter = inputConverter;
        action = new RequesterAction<Input, Output>(
                onDemandCacheRequester,
                this.inputConverter,
                outputConverter
        );
    }

    public void expire(Input input) {
        onDemandCacheRequester.expire(inputConverter.compose(input));
    }

    @Override
    public Output apply(Input input) throws RuntimeException {
        return action.apply(input);
    }
}
