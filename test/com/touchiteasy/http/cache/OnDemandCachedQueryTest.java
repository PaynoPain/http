package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.commons.Function;
import com.touchiteasy.commons.MutableFactory;
import com.touchiteasy.http.*;
import com.touchiteasy.http.actions.RequestComposer;
import com.touchiteasy.http.actions.ResponseInterpreter;
import com.touchiteasy.http.validation.ResponseValidator;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class OnDemandCachedQueryTest {
    MutableFactory<Date> timeGateway;
    ServerMock server;
    MapStorage<Request, CacheEntry> storage;
    Function<String, String> query;
    Long cacheDuration, timeToRefresh;

    @Before
    public void GivenAServer(){
        server = new ServerMock();
        timeGateway = new MutableFactory<Date>(null);
        cacheDuration = 100L;
        timeToRefresh = 200L;
        storage = new MapStorageInMemory();

        final RequestComposer<String> requestComposer = new RequestComposer<String>() {
            @Override
            public Request compose(String resource) {
                return new BaseRequest(resource);
            }
        };
        final ResponseInterpreter<String> responseInterpreter = new ResponseInterpreter<String>() {
            @Override
            public String interpret(Response response) throws IllegalArgumentException {
                return response.getBody();
            }
        };
        final ResponseValidator cacheValidator = new ResponseValidator() {
            @Override
            protected ValidationResult analyse(Response response) {
                if (response.getStatusCode() != 200)
                    return ValidationResult.invalid("The response status code should be 200 (OK).");

                if (response.getBody().equals("::error::"))
                    return ValidationResult.invalid("The response body should not contain an error.");

                return ValidationResult.valid();
            }
        };
        query = new OnDemandCachedQuery<String, String>(
                server, storage, cacheValidator,
                requestComposer, responseInterpreter,
                timeGateway,
                cacheDuration, timeToRefresh
        );
    }

    @Test (expected = RuntimeException.class)
    public void GivenAnInvalidBody_ShouldThrowException() {
        server.addResponse(new BaseResponse(200, "::error::"));
        query.apply("::input::");
    }

    @Test (expected = RuntimeException.class)
    public void GivenAnInvalidStatusCode_ShouldThrowException() {
        server.addResponse(new BaseResponse(500, "::ok::"));
        query.apply("::input::");
    }

    @Test (expected = RuntimeException.class)
    public void GivenTheServerIsUnavailable_ShouldThrowException() {
        server.addResponseFactory(new Factory<Response>() {
            @Override
            public Response get() {
                throw new RuntimeException();
            }
        });
        query.apply("::input::");
    }

    @Test
    public void GivenAValidResponse_ShouldInterpretIt() {
        server.addResponse(new BaseResponse(200, "::response_body::"));
        timeGateway.set(new Date(0L));

        String result = query.apply("::input::");

        assertThat(result, is("::response_body::"));
        assertThat(server.requests.size(), is(1));
        assertThat(server.requests.get(0).getResource(), is("::input::"));
        assertThat(storage.contains(server.requests.get(0)), is(true));
    }

    @Test
    public void GivenAnInvalidResponse_ShouldNotCacheIt() {
        server.addResponse(new BaseResponse(200, "::error::"));
        try {
            query.apply("::input::");
        } catch (RuntimeException ignored) {}
        assertThat(storage.contains(server.requests.get(0)), is(false));
    }
}
