package com.touchiteasy.oauth;

import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RequesterSpy implements ResourceRequester {
    public boolean runHasBeenCalled = false;
    public Request receivedRequest = null;

    @Override
    public Response run(Request request) {
        runHasBeenCalled = true;
        receivedRequest = request;
        return null;
    }
}

class EmptyTokensStorage implements TokensStorage {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Tokens get() {
        return null;
    }

    @Override
    public void set(Tokens t) {}
}

class TokensStorageWithData implements TokensStorage {
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Tokens get() {
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
    public void set(Tokens t) {}
}

@RunWith(HierarchicalContextRunner.class)
public class InteractorTest {
    Interactor i;

    public class GivenARequesterSpy {
        RequesterSpy originRequester;

        @Before
        public void setUp(){
            originRequester = new RequesterSpy();
        }

        public class AndAnEmptyTokensStorage {
            TokensStorage storage;

            @Before
            public void setUp(){
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
                public void setUp(){
                    i = new Interactor(originRequester, storage, client, user);
                }

                public class WhenRunningARequestWithOneParameter {
                    public static final String BASE_RESOURCE = "http://www.google.es";
                    public static final String BASE_PARAMETER = "base parameter";
                    public static final String BASE_VALUE = "base value";

                    Response r;
                    Request baseRequest;

                    @Before
                    public void setUp(){
                        baseRequest = new BaseRequest(BASE_RESOURCE, new LiteralStringsMap(BASE_PARAMETER, BASE_VALUE));
                        r = i.run(baseRequest);
                    }

                    @Test
                    public void shouldCallTheOriginRequesterToLogin(){
                        assertThat(originRequester.runHasBeenCalled, is(true));
                    }

                    @Test
                    public void shouldReceiveTheBaseResource(){
                        assertThat(originRequester.receivedRequest.getResource(), is(BASE_RESOURCE));
                    }

                    public class ShouldReceiveTheParameters {
                        Map<String, String> parameters;

                        @Before
                        public void setUp(){
                            parameters = originRequester.receivedRequest.getParameters();
                        }

                        public class RequiredToLogin {
                            @Test
                            public void shouldReceiveThePasswordGrantType(){
                                assertThat(parameters.containsKey("grant_type"), is(true));
                                assertThat(parameters.get("grant_type"), is("password"));
                            }

                            @Test
                            public void shouldReceiveTheUsername(){
                                assertThat(parameters.containsKey("username"), is(true));
                                assertThat(parameters.get("username"), is(USERNAME));
                            }

                            @Test
                            public void shouldReceiveThePassword(){
                                assertThat(parameters.containsKey("password"), is(true));
                                assertThat(parameters.get("password"), is(PASSWORD));
                            }

                            @Test
                            public void shouldReceiveTheClientId(){
                                assertThat(parameters.containsKey("client_id"), is(true));
                                assertThat(parameters.get("client_id"), is(CLIENT_ID));
                            }

                            @Test
                            public void shouldReceiveTheClientSecret(){
                                assertThat(parameters.containsKey("client_secret"), is(true));
                                assertThat(parameters.get("client_secret"), is(CLIENT_SECRET));
                            }
                        }

                        public class OwnedByTheBaseRequest {
                            @Test
                            public void shouldReceiveTheBaseParameter(){
                                assertThat(parameters.containsKey(BASE_PARAMETER), is(true));
                                assertThat(parameters.get(BASE_PARAMETER), is(BASE_VALUE));
                            }
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
            }


            public class AndTheRemainingRequiredThings {
                @Before
                public void setUp(){
                    i = new Interactor(originRequester, storage, new Client("ignore", "ignore"), new User("ignore", "ignore"));
                }

                public class WhenRunningARequestWithOneParameter {
                    @Test
                    public void todo(){
                        fail("Implement");
                    }
                }
            }
        }*/
    }
}
