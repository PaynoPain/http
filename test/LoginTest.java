import de.bechte.junit.runners.context.HierarchicalContextRunner;
import http.Request;
import http.ResourceRequester;
import http.Response;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class ResourceRequesterSpy implements ResourceRequester {
    public Request receivedRequest;
    @Override
    public Response run(Request request) {
        this.receivedRequest = request;
        return new Response(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}");
    }
}
class ParserSpy extends TokensParser {
    public Response ReceivedResponse;
    @Override
    public Tokens constructFrom(Response response) throws InvalidTokensResponse {
        this.ReceivedResponse = response;
        return new Tokens("f820a39359b7a69436b1c1fdad01a6afbad27f38", "6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8");
    }
}

@RunWith(HierarchicalContextRunner.class)
public class LoginTest {
    public class GivenARequesterSpy {
        ResourceRequesterSpy requester;
        ParserSpy parser;

        @Before
        public void setUp() {
            requester = new ResourceRequesterSpy();
            parser = new ParserSpy();
        }

        public class WhenLoginIsExecutedWithSpies {
            Login login;
            Tokens tokens;

            @Before
            public void setUp() throws IOException {
                login = new Login(requester, parser, "paco@paquito.pa", "paco");
                tokens = login.execute();
            }

            @Test
            public void shouldReturnRequesterParameters() throws IOException {
                assertThat(requester.receivedRequest.parameters.containsKey("grant_type"), is(true));
                assertThat(requester.receivedRequest.parameters.get("grant_type"), is("password"));

                assertThat(requester.receivedRequest.parameters.containsKey("username"), is(true));
                assertThat(requester.receivedRequest.parameters.get("username"), is("paco@paquito.pa"));

                assertThat(requester.receivedRequest.parameters.containsKey("password"), is(true));
                assertThat(requester.receivedRequest.parameters.get("password"), is("paco"));

                assertThat(requester.receivedRequest.parameters.containsKey("client_id"), is(true));
                assertThat(requester.receivedRequest.parameters.get("client_id"), is("NTM0NjQ4NjlhNGY0Yzgz"));

                assertThat(requester.receivedRequest.parameters.containsKey("client_secret"), is(true));
                assertThat(requester.receivedRequest.parameters.get("client_secret"), is("b4c4bb62dac450925d7cf3322a9d4e8fde7bc104"));
            }
            @Test
            public void shouldReturnAccessToken() throws IOException {
                assertThat(tokens.access, is("f820a39359b7a69436b1c1fdad01a6afbad27f38"));
            }

            @Test
            public void shouldReturnRefreshToken() throws IOException {
                assertThat(tokens.refresh, is("6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8"));
            }
        }
    }
}
