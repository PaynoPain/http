package com.touchiteasy.http.cache;

import com.touchiteasy.commons.Factory;
import com.touchiteasy.commons.MutableFactory;
import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

@RunWith(HierarchicalContextRunner.class)
public class OnDemandCacheRequesterTest {
    MutableFactory<Date> timeGateway;
    ServerMock server;
    ResourceRequester cache;
    Long cacheDuration, timeToRefresh;

    @Before
    public void GivenAServer(){
        server = new ServerMock();
        timeGateway = new MutableFactory<Date>(null);
        cacheDuration = 100L;
        timeToRefresh = 200L;
        cache = new OnDemandCacheRequester(server, new CacheStorageInMemory(), timeGateway, cacheDuration, timeToRefresh);
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
            private Request createSameRequest() {
                return new Request() {
                    @Override
                    public String getResource() {
                        return firstRequest.getResource();
                    }

                    @Override
                    public Map<String, String> getParameters() {
                        return firstRequest.getParameters();
                    }
                };
            }

            public class JustBeforeCacheExpires {
                Long beforeExpiredTime;

                @Before
                public void ChangeTime(){
                    beforeExpiredTime = firstRequestTime + cacheDuration;
                    timeGateway.set(new Date(beforeExpiredTime));
                }

                @Test
                public void ShouldNotAskTheServerAgain(){
                    Request sameRequest = createSameRequest();
                    cache.run(sameRequest);
                    assertThat(server.requests.size(), is(1));
                }
            }

            public class JustAfterCacheExpired {
                Long afterExpiredTime;

                @Before
                public void ChangeTime(){
                    afterExpiredTime = firstRequestTime + cacheDuration + 1;
                    timeGateway.set(new Date(afterExpiredTime));
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

                    public class AndSameRequestBeforeNewExpiration {
                        @Before
                        public void ChangeTime(){
                            long beforeNewExpiration = afterExpiredTime + cacheDuration;
                            timeGateway.set(new Date(beforeNewExpiration));
                        }

                        @Test
                        public void ShouldNotAskTheServerAgain(){
                            Request sameRequest = createSameRequest();
                            cache.run(sameRequest);
                            assertThat(server.requests.size(), is(2));
                        }
                    }

                    public class AndSameRequestAfterNewExpiration {
                        @Before
                        public void ChangeTime(){
                            long afterNewExpiration = afterExpiredTime + cacheDuration +1;
                            timeGateway.set(new Date(afterNewExpiration));
                        }

                        @Test
                        public void ShouldNotAskTheServerAgain(){
                            Request sameRequest = createSameRequest();
                            cache.run(sameRequest);
                            assertThat(server.requests.size(), is(3));
                        }
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

            public class JustAfterDeadline {
                Long afterDeadlineTime;

                @Before
                public void ChangeTime(){
                    afterDeadlineTime = firstRequestTime + cacheDuration + timeToRefresh + 1;
                    timeGateway.set(new Date(afterDeadlineTime));
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
