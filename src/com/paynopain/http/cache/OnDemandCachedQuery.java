package com.paynopain.http.cache;

import com.paynopain.commons.Factory;
import com.paynopain.commons.Function;
import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.actions.RequestComposer;
import com.paynopain.http.actions.RequesterAction;
import com.paynopain.http.actions.ResponseInterpreter;
import com.paynopain.http.validation.ResponseValidatingRequester;
import com.paynopain.http.validation.ResponseValidator;

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
