package com.touchiteasy.oauth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RequesterSpy implements ResourceRequester{
    List<Response> responses = new ArrayList<Response>();
    List<Request> requests = new ArrayList<Request>();

    private int responseIndex = 0;

    @Override
    public Response run(Request request){
        requests.add(request);

        Response r = responses.get(responseIndex);
        responseIndex++;
        return r;
    }
}

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
    public void setUp(){
        client = new Client ("clientId", "clientSecret");
        user = new User("username", "password");
    }

    public class GivenARequesterSpy {
        RequesterSpy originRequester;
        @Before
        public void setUp(){
            originRequester = new RequesterSpy();
        }
        public class AndATokensStorageWithData {
            TokensStorageWithData storage;
            @Before
            public void setUp(){
                storage = new TokensStorageWithData();
            }
            public class WhenRunARequest{
                @Before
                public void setUp(){
                    interactor = new Interactor(originRequester, storage, client, user, GET_TOKENS_URL);
                }
                public class AndResponseCodeIs200{
                    @Before
                    public void setUp(){
                        originRequester.responses.add(new BaseResponse(200, "Success"));
                    }
                    @Test
                    public void shouldPassTheAccessTokenParameter(){
                        runRequest();
                        assertThat(originRequester.requests.get(0).getParameters().containsKey("access_token"), is(true));
                        assertThat(originRequester.requests.get(0).getParameters().get("access_token"),is("access"));
                    }
                    @Test
                    public void shouldReturnResponseCode(){
                        assertThat(runRequest().getStatusCode(), is(originRequester.responses.get(0).getStatusCode()));
                    }
                    @Test
                    public void shouldReturnResponseBody(){
                        assertThat(runRequest().getBody(), is(originRequester.responses.get(0).getBody()));
                    }
                }
                public class AndResponseCodeIs400{
                    @Before
                    public void setUp(){
                        originRequester.responses.add(new BaseResponse(400, "Fail"));
                    }
                    @Test
                    public void shouldReturnResponseCode(){
                        assertThat(runRequest().getStatusCode(), is(originRequester.responses.get(0).getStatusCode()));
                    }
                    @Test
                    public void shouldReturnResponseBody(){
                        assertThat(runRequest().getBody(), is(originRequester.responses.get(0).getBody()));
                    }
                }
            }
        }

        public class AndAnEmptyTokensStorage {
            EmptyTokensStorage storage;
            @Before
            public void setUp(){
                storage = new EmptyTokensStorage();
            }
            public class WhenRunALoginRequest{
                @Before
                public void setUp(){
                    interactor = new Interactor(originRequester, storage, client, user, GET_TOKENS_URL);
                }
                public class AndResponseCodeIs400{
                    @Before
                    public void setUp(){
                        originRequester.responses.add(new BaseResponse(401, "Authentication Error"));
                    }
                    @Test(expected = AuthenticationError.class)
                    public void shouldReturnResponseBody(){
                        runRequest();
                    }
                }
                public class AndResponseCodeIs401{
                    @Before
                    public void setUp(){
                        originRequester.responses.add(new BaseResponse(400, "Internal Error"));
                    }
                    @Test(expected = InternalError.class)
                    public void shouldReturnResponseBody(){
                        runRequest();
                    }
                }
                public class AndResponseCodeIs200{
                    @Before
                    public void setUp(){
                        originRequester.responses.add(new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}"));
                        originRequester.responses.add(new BaseResponse(200, "Success"));
                    }
                    @Test
                    public void shouldCallSetToken(){
                        runRequest();
                        assertThat(storage.isSetCalled, is(true));
                    }
                    @Test
                    public void shouldSetAccessTokenProperly(){
                        runRequest();
                        assertThat(storage.get().getAccess(), is("f820a39359b7a69436b1c1fdad01a6afbad27f38"));
                    }
                    @Test
                    public void shouldSetRefreshTokenProperly(){
                        runRequest();
                        assertThat(storage.get().getRefresh(), is("6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8"));
                    }
                    public class WhenRunAFinalRequest{
                        public class AndFinalResponseCodeIs200{
                            @Test
                            public void shouldReturnResponseCode(){
                                assertThat(runRequest().getStatusCode(), is(originRequester.responses.get(1).getStatusCode()));
                            }
                            @Test
                            public void shouldReturnResponseBody(){
                                assertThat(runRequest().getBody(), is(originRequester.responses.get(1).getBody()));
                            }
                        }
                        public class AndFinalResponseCodeIs400{
                            @Before
                            public void setUp(){
                                originRequester.responses.set(1, new BaseResponse(400, "Fail"));
                            }
                            @Test
                            public void shouldReturnResponseCode(){
                                assertThat(runRequest().getStatusCode(), is(originRequester.responses.get(1).getStatusCode()));
                            }
                            @Test
                            public void shouldReturnResponseBody(){
                                assertThat(runRequest().getBody(), is(originRequester.responses.get(1).getBody()));
                            }
                        }
                        public class AndFinalResponseCodeIs401{
                            @Before
                            public void setUp(){
                                originRequester.responses.set(1, new BaseResponse(401, "Auth Error"));
                            }
                            @Test(expected = AuthenticationError.class)
                            public void shouldReturnResponseBody(){
                                runRequest();
                            }
                        }
                    }
                }
            }
        }
    }

    private Response runRequest() {
        return interactor.run(new BaseRequest("www.touchItEasy.com/getPermission.json"));
    }
}