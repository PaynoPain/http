package com.touchiteasy.http.context.oauth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class EmptyTokensStorage implements TokensStorage {
    public boolean isEmptyCalled = false;
    public boolean isSetCalled = false;
    String access;
    String refresh;

    @Override
    public boolean isEmpty() {
        isEmptyCalled = true;
        return true;
    }

    @Override
    public Tokens get() {

        return new Tokens() {
            @Override
            public String getAccess() {
                return access;
            }

            @Override
            public String getRefresh() {
                return refresh;
            }
        };

    }

    @Override
    public void set(Tokens t) {
        isSetCalled = true;
        access = t.getAccess();
        refresh = t.getRefresh();

    }
}


class TokensStorageWithData implements TokensStorage {
    public boolean isEmptyCalled = false;
    public boolean isGetCalled = false;
    public boolean isSetCalled = false;
    String access;
    String refresh;

    @Override
    public boolean isEmpty() {
        isEmptyCalled = true;
        return false;
    }

    @Override
    public Tokens get() {
        isGetCalled = true;
        return new Tokens() {
            @Override
            public String getAccess() {
                return "access";
            }

            @Override
            public String getRefresh() {
                return "refresh";
            }
        };

    }

    @Override
    public void set(Tokens t) {
        isSetCalled = true;
        access = t.getAccess();
        refresh = t.getRefresh();
    }
}

@RunWith(HierarchicalContextRunner.class)
public class InteractorTest {
    Interactor interactor;
    Client client;
    User user;

    static final String GET_TOKENS_URL = "http://www.touchItEasy.com/oauth/token";

    @Before
    public void setUp() {
        client = new Client("clientId", "clientSecret");
        user = new User("username", "password");
    }

    public class GivenARequesterSpy {
        RequesterMock resourceRequester;
        RequesterMock postRequester;

        @Before
        public void setUp() {
            resourceRequester = new RequesterMock();
            postRequester = new RequesterMock();
        }

        public class AndATokensStorageWithData {
            TokensStorageWithData storage;

            @Before
            public void setUp() {
                storage = new TokensStorageWithData();
            }

            public class WhenRunARequest {
                @Before
                public void setUp() {
                    interactor = new Interactor(resourceRequester, resourceRequester, storage, client, user, GET_TOKENS_URL);
                }

                public class AndResponseCodeIs200 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(200, "Success"));
                    }

                    @Test
                    public void shouldPassTheAccessTokenParameter() {
                        runRequest();
                        assertThat(resourceRequester.requests.get(0).getParameters().containsKey("access_token"), is(true));
                        assertThat(resourceRequester.requests.get(0).getParameters().get("access_token"), is("access"));
                    }

                    @Test
                    public void shouldReturnResponseCode() {
                        assertThat(runRequest().getStatusCode(), is(resourceRequester.responses.get(0).getStatusCode()));
                    }

                    @Test
                    public void shouldReturnResponseBody() {
                        assertThat(runRequest().getBody(), is(resourceRequester.responses.get(0).getBody()));
                    }
                }

                public class AndResponseCodeIs400 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(400, "Fail"));
                    }

                    @Test
                    public void shouldReturnResponseCode() {
                        assertThat(runRequest().getStatusCode(), is(resourceRequester.responses.get(0).getStatusCode()));
                    }

                    @Test
                    public void shouldReturnResponseBody() {
                        assertThat(runRequest().getBody(), is(resourceRequester.responses.get(0).getBody()));
                    }
                }

                public class AndResponseCodeIs401 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(401, "Access token expired "));
                        resourceRequester.responses.add(new BaseResponse(200, "Success"));
                    }

                    public class WhenRunARefreshRequest {
                        public class AndResponseCodeIs400 {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(400, "Internal Error"));
                            }

                            @Test(expected = InternalError.class)
                            public void shouldReturnAnInternalErrorException() {
                                runRequest();
                            }
                        }

                        public class AndResponseCodeIs401Again {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(401, "Refresh Fail"));
                                resourceRequester.responses.add(new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                                resourceRequester.responses.add(new BaseResponse(200, "Successfull FinalRequest"));
                            }

                            @Test
                            public void shouldReturnCorrectFinalResponseBody() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(3).getBody()));
                            }

                            @Test
                            public void shouldReturnCorrectFinalResponseCode() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(3).getBody()));
                            }
                        }

                        public class AndResponseCodeIs200 {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                                resourceRequester.responses.add(new BaseResponse(200, "Successfull FinalRequest"));
                            }

                            @Test
                            public void setTokenShouldBeCalled() {
                                runRequest();
                                assertThat(storage.isSetCalled, is(true));
                            }

                            @Test
                            public void shouldReturnCorrectFinalResponseBody() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(2).getBody()));
                            }

                            @Test
                            public void shouldReturnCorrectFinalResponseCode() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(2).getBody()));
                            }

                        }

                        public class AndResponseCodeIs200ButResponseIsWrong {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(200, "{}"));
                            }

                            @Test(expected = InvalidTokensResponse.class)
                            public void setTokenShouldBeCalled() {
                                runRequest();
                            }
                        }
                    }
                }
            }
        }

        public class AndAnEmptyTokensStorage {
            EmptyTokensStorage storage;

            @Before
            public void setUp() {
                storage = new EmptyTokensStorage();
            }

            public class WhenRunALoginRequest {
                @Before
                public void setUp() {
                    interactor = new Interactor(resourceRequester, resourceRequester, storage, client, user, GET_TOKENS_URL);
                }

                public class AndResponseCodeIs400 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(401, "Authentication Error"));
                    }

                    @Test(expected = AuthenticationError.class)
                    public void shouldReturnResponseBody() {
                        runRequest();
                    }
                }

                public class AndResponseCodeIs401 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(400, "Internal Error"));
                    }

                    @Test(expected = InternalError.class)
                    public void shouldReturnResponseBody() {
                        runRequest();
                    }
                }

                public class AndResponseCodeIs200 {
                    @Before
                    public void setUp() {
                        resourceRequester.responses.add(new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                        resourceRequester.responses.add(new BaseResponse(200, "Success"));
                    }

                    @Test
                    public void shouldCallSetToken() {
                        runRequest();
                        assertThat(storage.isSetCalled, is(true));
                    }

                    @Test
                    public void shouldSetAccessTokenProperly() {
                        runRequest();
                        assertThat(storage.get().getAccess(), is("f820a39359b7a69436b1c1fdad01a6afbad27f38"));
                    }

                    @Test
                    public void shouldSetRefreshTokenProperly() {
                        runRequest();
                        assertThat(storage.get().getRefresh(), is("6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8"));
                    }

                    public class WhenRunAFinalRequest {
                        public class AndFinalResponseCodeIs200 {
                            @Test
                            public void shouldReturnResponseCode() {
                                assertThat(runRequest().getStatusCode(), is(resourceRequester.responses.get(1).getStatusCode()));
                            }

                            @Test
                            public void shouldReturnResponseBody() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(1).getBody()));
                            }
                        }

                        public class AndFinalResponseCodeIs400 {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(400, "Fail"));
                            }

                            @Test
                            public void shouldReturnResponseCode() {
                                assertThat(runRequest().getStatusCode(), is(resourceRequester.responses.get(1).getStatusCode()));
                            }

                            @Test
                            public void shouldReturnResponseBody() {
                                assertThat(runRequest().getBody(), is(resourceRequester.responses.get(1).getBody()));
                            }
                        }

                        public class AndFinalResponseCodeIs401 {
                            @Before
                            public void setUp() {
                                resourceRequester.responses.set(1, new BaseResponse(401, "Auth Error"));
                            }

                            @Test(expected = AuthenticationError.class)
                            public void shouldReturnResponseBody() {
                                runRequest();
                            }
                        }
                    }
                }
            }
        }

        public class WhenRunAResourceRequest {
            TokensStorageWithData storage;

            @Before
            public void setUp() {
                storage = new TokensStorageWithData();
                interactor = new Interactor(resourceRequester, postRequester, storage, client, user, GET_TOKENS_URL);
                resourceRequester.responses.add(new BaseResponse(401, "need refresh"));
                postRequester.responses.add(new BaseResponse(200,"{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                resourceRequester.responses.add(new BaseResponse(200,"Ok"));
            }

            @Test
            public void shouldCallResourceRequesterTwice() {
                runRequest();
                assertThat(resourceRequester.requests.size(), is(2));
            }
            @Test
            public void shouldCallResourceRequesterOnce() {
                runRequest();
                assertThat(postRequester.requests.size(), is(1));
            }

        }
        public class WhenRunARequest {
            EmptyTokensStorage storage;

            @Before
            public void setUp() {
                storage = new EmptyTokensStorage();
                interactor = new Interactor(resourceRequester, postRequester, storage, client, user, GET_TOKENS_URL);
                postRequester.responses.add(new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                resourceRequester.responses.add(new BaseResponse(200, "ok"));

            }

            @Test
            public void shouldCallOauthRequesterOnce() {
                runRequest();
                assertThat(postRequester.requests.size(), is(1));

            }
            @Test
            public void shouldCallResourceRequesterOnce() {
                runRequest();
                assertThat(resourceRequester.requests.size(), is(1));

            }
        }

        public class WhenCheckingIfCanLogin {
            @Before
            public void setUp() {
                EmptyTokensStorage storage = new EmptyTokensStorage();
                interactor = new Interactor(resourceRequester, postRequester, storage, client, user, GET_TOKENS_URL);
            }

            public class WithOkResponseFromServer {
                @Before
                public void setUp(){
                    postRequester.responses.add(
                            new BaseResponse(
                                    200,
                                    "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"
                            )
                    );
                }

                @Test
                public void shouldReturnTrue(){
                    assertThat(interactor.canLogin(), is(true));
                }
            }

            public class WithAuthErrorFromServer {
                @Before
                public void setUp(){
                    postRequester.responses.add(
                            new BaseResponse(
                                    400,
                                    "{\"error\":\"invalid_client\",\"error_description\":\"The client credentials are invalid\"}"
                            )
                    );
                }

                @Test
                public void shouldReturnFalse(){
                    assertThat(interactor.canLogin(), is(false));
                }
            }
        }
    }

    private Response runRequest() {
        return interactor.run(new BaseRequest("www.touchItEasy.com/getPermission.json"));
    }
}