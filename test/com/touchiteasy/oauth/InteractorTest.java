package com.touchiteasy.oauth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RequesterSpy implements ResourceRequester {
    final String responseBody;
    final int responseCode;

    public RequesterSpy(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public boolean runHasBeenCalled = false;
    public Request receivedRequest;
    public ArrayList<Request> differentRequest = new ArrayList<Request>();

    @Override
    public Response run(Request request) {
        runHasBeenCalled = true;
        receivedRequest = request;
        differentRequest.add(receivedRequest);
        return new BaseResponse(responseCode, responseBody);
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
    public boolean isGetAccessCalled = false;
    public boolean isGetRefreshCalled = false;
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

        return new Tokens() {
            @Override
            public String getAccess() {
                isGetAccessCalled = true;
                return "access";
            }

            @Override
            public String getRefresh() {
                isGetRefreshCalled = true;
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

    public class GivenARequesterSpy {
        RequesterSpy originRequester;

        public class AndAnEmptyTokensStorage {
            EmptyTokensStorage storage;

            @Before
            public void setUp() {
                originRequester = new RequesterSpy(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}");
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

                public class WhenRunningARequest {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    public static final String BASE_PARAMETER = "base parameter";
                    public static final String BASE_VALUE = "base value";

                    Response r;
                    Request baseRequest;

                    @Before
                    public void setUp() throws IOException {
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
                            parameters = originRequester.differentRequest.get(0).getParameters();
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

                    public class ShouldStorageTokensInTokenStorage {
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

        public class AndATokensStorageWithData {
            TokensStorageWithData storage;

            @Before
            public void setUp() {
                storage = new TokensStorageWithData();

            }

            public class WhenResponseCodeIs200 {
                @Before
                public void setUp() {
                    originRequester = new RequesterSpy(200, "{\"result\":1,\"message\":\"OK\",\"data\":{\"id\":\"1\",\"name\":\"Paco\",\"lastname\":\"Paquito\",\"mail\":\"paco@paquito.pa\",\"phone\":\"33670678087\"}}");
                    interactor = new Interactor(originRequester, storage, new Client("ignore", "ignore"), new User("ignore", "ignore"));
                }

                public class WhenRunningARequest {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    Response response;
                    Request baseRequest;

                    @Before
                    public void setUp() throws IOException {
                        baseRequest = new BaseRequest(BASE_RESOURCE);
                        interactor.run(baseRequest);
                    }

                    public class shouldRecieveTheParameters {
                        Map<String, String> parameters;

                        @Before
                        public void setUp() {
                            parameters = originRequester.differentRequest.get(0).getParameters();
                        }

                        @Test
                        public void shouldReceiveTheBaseResource() {
                            assertThat(originRequester.receivedRequest.getResource(), is(BASE_RESOURCE));
                        }

                        @Test
                        public void shouldReceiveAccessTokenGrantType() {
                            assertThat(parameters.containsKey("access_token"), is(true));
                            assertThat(parameters.get("access_token"), is("access"));
                        }
                    }

                    @Test
                    public void shouldCallIsEmpty() {
                        assertThat(storage.isEmptyCalled, is(true));
                    }

                    @Test
                    public void shouldCallGet() {
                        assertThat(storage.isGetAccessCalled, is(true));
                    }
                }
            }

            public class WhenResponseCodeIs401 {
                public static final String CLIENT_ID = "AccessControl";
                public static final String CLIENT_SECRET = "not so secret";

                Client client;

                @Before
                public void setUp() {


                    client = new Client(CLIENT_ID, CLIENT_SECRET);
                    originRequester = new RequesterSpy(401, "{\"error\":\"invalid_grant\",\"error_description\":\"The access token provided has expired.\"}");

                    interactor = new Interactor(originRequester, storage, client, new User("ignore", "ignore"));
                }

                public class WhenRunningARequest {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    Request baseRequest;

                    @Before
                    public void setUp() throws IOException {
                        baseRequest = new BaseRequest(BASE_RESOURCE);
                        interactor.run(baseRequest);
                    }

                    public class shouldRecieveTheParameters {
                        Map<String, String> parameters;

                        @Before
                        public void setUp() {
                            parameters = originRequester.differentRequest.get(1).getParameters();
                        }

                        @Test
                        public void shouldReceiveTheBaseResource() {
                            assertThat(originRequester.receivedRequest.getResource(), is(BASE_RESOURCE));
                        }

                        @Test
                        public void shouldReceiveAccessTokenGrantType() {
                            assertThat(parameters.containsKey("refresh_token"), is(true));
                            assertThat(parameters.get("refresh_token"), is("refresh"));
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
                    @Test
                    public void shouldCallSetTokens(){
                        assertThat(storage.isSetCalled, is(true));
                    }
                }
            }

            public class WhenResponseCodeIs400 {
                @Before
                public void setUp() {
                    originRequester = new RequesterSpy(400, "{\"error\":\"invalid_grant\",\"error_description\":\"The access token provided is invalid.\"}");
                    interactor = new Interactor(originRequester, storage, new Client("ignore", "ignore"), new User("ignore", "ignore"));
                }

                public class WhenRunningARequest {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    Request baseRequest;

                    @Before
                    public void setUp() throws IOException {
                        baseRequest = new BaseRequest(BASE_RESOURCE);
                    }

                    @Test(expected = IOException.class)
                    public void shouldThrowIOException() throws IOException {
                        interactor.run(baseRequest);
                    }
                }
            }
        }
    }
}