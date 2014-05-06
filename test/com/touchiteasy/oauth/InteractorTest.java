package com.touchiteasy.oauth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RequesterSpy implements ResourceRequester {
    final static String RESPONSE = "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}";
    public boolean runHasBeenCalled = false;
    public Request receivedRequest = null;

    @Override
    public Response run(Request request) {
        runHasBeenCalled = true;
        receivedRequest = request;
        return new BaseResponse(200,RESPONSE);
    }
}

class EmptyTokensStorage implements TokensStorage{
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
        access =t.getAccess();
        refresh = t.getRefresh();

    }
}

@RunWith(HierarchicalContextRunner.class)
public class InteractorTest {
    Interactor interactor;

    public class GivenARequesterSpy {

        RequesterSpy originRequester;

        @Before
        public void setUp() {
            originRequester = new RequesterSpy();
        }

        public class AndAnEmptyTokensStorage {
            EmptyTokensStorage storage;

            @Before
            public void setUp() {
                storage = new EmptyTokensStorage();
            }

            public class AndTheRemainingRequiredThings {
                public static final String USERNAME = "John";
                public static final String PASSWORD = "doe";
                public static final String CLIENT_ID = "AccessControl";
                public static final String CLIENT_SECRET = "not so secret";

                Client client = new Client(CLIENT_ID, CLIENT_SECRET);
                User user = new User(USERNAME, PASSWORD);

                @Before
                public void setUp() {
                    interactor = new Interactor(originRequester, storage, client, user);
                }

                public class WhenRunningARequestWithOneParameter {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    public static final String BASE_PARAMETER = "base parameter";
                    public static final String BASE_VALUE = "base value";

                    Response r;
                    Request baseRequest;

                    @Before
                    public void setUp() {
                        baseRequest = new BaseRequest(BASE_RESOURCE, new LiteralStringsMap(BASE_PARAMETER, BASE_VALUE));
                        r = interactor.run(baseRequest);
                    }

                    @Test
                    public void shouldCallTheOriginRequesterToLogin() {
                        assertThat(originRequester.runHasBeenCalled, is(true));
                    }

                    @Test
                    public void shouldReceiveTheBaseResource() {
                        assertThat(originRequester.receivedRequest.getResource(), is(BASE_RESOURCE));
                    }

                    public class ShouldReceiveTheParameters {
                        Map<String, String> parameters;

                        @Before
                        public void setUp() {
                            parameters = originRequester.receivedRequest.getParameters();
                        }

                        public class RequiredToLogin {
                            @Test
                            public void shouldReceiveThePasswordGrantType() {
                                assertThat(parameters.containsKey("grant_type"), is(true));
                                assertThat(parameters.get("grant_type"), is("password"));
                            }

                            @Test
                            public void shouldReceiveTheUsername() {
                                assertThat(parameters.containsKey("username"), is(true));
                                assertThat(parameters.get("username"), is(USERNAME));
                            }

                            @Test
                            public void shouldReceiveThePassword() {
                                assertThat(parameters.containsKey("password"), is(true));
                                assertThat(parameters.get("password"), is(PASSWORD));
                            }

                            @Test
                            public void shouldReceiveTheClientId() {
                                assertThat(parameters.containsKey("client_id"), is(true));
                                assertThat(parameters.get("client_id"), is(CLIENT_ID));
                            }

                            @Test
                            public void shouldReceiveTheClientSecret() {
                                assertThat(parameters.containsKey("client_secret"), is(true));
                                assertThat(parameters.get("client_secret"), is(CLIENT_SECRET));
                            }
                        }

                        public class OwnedByTheBaseRequest {
                            @Test
                            public void shouldReceiveTheBaseParameter() {
                                assertThat(parameters.containsKey(BASE_PARAMETER), is(true));
                                assertThat(parameters.get(BASE_PARAMETER), is(BASE_VALUE));
                            }
                        }
                    }

                    public class ShouldStorageTokensInTokenStorage{
                        @Test
                        public void AndCallIsEmpty() {
                            assertThat(storage.isEmptyCalled, is(true));
                        }

                        @Test
                        public void AndCallSetToken() {
                            assertThat(storage.isSetCalled, is(true));
                        }

                        @Test
                        public void getTokensShouldReturnAccessTokens() {
                            assertThat(storage.get().getAccess(), is("f820a39359b7a69436b1c1fdad01a6afbad27f38"));
                        }

                        @Test
                        public void getTokensShouldReturnRefreshTokens() {
                            assertThat(storage.get().getRefresh(), is("6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8"));
                        }
                    }
                }
            }
        }
        /*public class AndATokensStorageWithData {
            TokensStorage storage;

            @Before
            public void setUp(){
                storage = new TokensStorageWithData();
                interactor = new Interactor(originRequester, storage, new Client("ignore", "ignore"), new User("ignore", "ignore"));
            }

            public class WhenRunningARequestWithOneParameter {
                @Test
                public void todo(){
                    fail();
                }
            }
        }*/
    }
}