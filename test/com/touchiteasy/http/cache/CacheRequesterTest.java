package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

class ServerMock implements ResourceRequester {
    public void addResponse(final Response response){
        responseFactories.add(new Factory<Response>() {
            @Override
            public Response get() {
                return response;
            }
        });
    }

    public void addResponseFactory(Factory<Response> responseFactory){
        responseFactories.add(responseFactory);
    }

    private List<Factory<Response>> responseFactories = new ArrayList<Factory<Response>>();
    public List<Request> requests = new ArrayList<Request>();

    private int responseIndex = 0;

    @Override
    public Response run(Request request) {
        requests.add(request);

        if (responseFactories.size() <= responseIndex)
            throw new RuntimeException("There aren't more responses configured!");

        Response r = responseFactories.get(responseIndex).get();
        responseIndex++;
        return r;
    }
}

class MutableFactory<T> implements Factory<T> {
    private T value;

    public MutableFactory(T initialValue){
        set(initialValue);
    }

    public void set(T value){
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}

@RunWith(HierarchicalContextRunner.class)
public class CacheRequesterTest {
    MutableFactory<Date> timeGateway;
    ServerMock server;
    ResourceRequester cache;
    Long cacheDuration;

    @Before
    public void GivenAServer(){
        server = new ServerMock();
        timeGateway = new MutableFactory<Date>(null);
        cacheDuration = 100L;
        cache = new CacheRequester(server, new CacheStorageInMemory(), timeGateway, cacheDuration);
    }

    public class IfTheServerIsUnavailable {
        RuntimeException serverException = new RuntimeException("Server unavailable!");

        @Before
        public void GivenAnException(){
            server.addResponseFactory(new Factory<Response>() {
                @Override
                public Response get() {
                    throw serverException;
                }
            });
        }

        @Test
        public void ShouldThrowTheExceptionFromTheServer(){
            RuntimeException thrownException = null;

            try {
                cache.run(new BaseRequest("firstRequest"));
            } catch (RuntimeException e){
                thrownException = e;
            }

            assertThat(thrownException, is(sameInstance(serverException)));
        }
    }

    public class WhenRequestingOneTime {
        Long firstRequestTime;
        Request firstRequest;
        Response mockResponse;
        Response actualResponse;

        @Before
        public void RequestWithTheFirstRequest(){
            firstRequestTime = 0L;
            timeGateway.set(new Date(firstRequestTime));

            mockResponse = new BaseResponse(200, "firstResponse");
            server.addResponse(mockResponse);

            firstRequest = new BaseRequest("firstRequest");
            actualResponse = cache.run(firstRequest);
        }

        @Test
        public void ShouldAskTheServer(){
            assertThat(server.requests.size(), is(1));
        }

        @Test
        public void ShouldAskTheServerForTheRequest() {
            assertThat(server.requests.get(0), is(sameInstance(firstRequest)));
        }

        @Test
        public void ShouldReturnTheResponseFromTheServer() {
            assertThat(actualResponse, is(sameInstance(mockResponse)));
        }

        public class AndThenTheSameRequestAgain {
            public class JustBeforeCacheExpires {
                @Before
                public void ChangeTime(){
                    timeGateway.set(new Date(firstRequestTime + cacheDuration));
                }

                @Test
                public void ShouldNotAskTheServerAgain(){
                    cache.run(firstRequest);
                    assertThat(server.requests.size(), is(1));
                }
            }

            public class AfterCacheExpired {
                @Before
                public void ChangeTime(){
                    timeGateway.set(new Date(firstRequestTime + cacheDuration +1));
                }

                public class IfTheServerRespondsOk{
                    Response newResponse;

                    @Before
                    public void GivenAnotherMockResponse(){
                        newResponse = new BaseResponse(200, "secondResponse");
                        server.addResponse(newResponse);

                        actualResponse = cache.run(firstRequest);
                    }

                    @Test
                    public void ShouldAskTheServerAgain(){
                        assertThat(server.requests.size(), is(2));
                    }

                    @Test
                    public void ShouldReturnTheResponseFromTheServer() {
                        assertThat(actualResponse, is(sameInstance(newResponse)));
                    }
                }

                public class IfTheServerIsUnavailable{
                    @Before
                    public void GivenAnException(){
                        server.addResponseFactory(new Factory<Response>() {
                            @Override
                            public Response get() {
                                throw new RuntimeException("Server unavailable!");
                            }
                        });

                        actualResponse = cache.run(firstRequest);
                    }

                    @Test
                    public void ShouldAskTheServerAgain(){
                        assertThat(server.requests.size(), is(2));
                    }

                    @Test
                    public void ShouldReturnThePreviouslyCachedResponse() {
                        assertThat(actualResponse, is(sameInstance(mockResponse)));
                    }
                }
            }
        }

        public class AndThenADifferentRequest {
            Request differentRequest;

            @Before
            public void RequestWithADifferentRequest(){
                mockResponse = new BaseResponse(200, "secondResponse");
                server.addResponse(mockResponse);

                differentRequest = new BaseRequest("differentRequest");
                actualResponse = cache.run(differentRequest);
            }

            @Test
            public void ShouldAskTheServerForTheNewResponse(){
                assertThat(server.requests.size(), is(2));
            }

            @Test
            public void ShouldReturnTheResponseFromTheServer() {
                assertThat(actualResponse, is(sameInstance(mockResponse)));
            }
        }
    }
}
