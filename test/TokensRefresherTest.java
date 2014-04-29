import de.bechte.junit.runners.context.HierarchicalContextRunner;
import http.Request;
import http.ResourceRequester;
import http.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class LoginRefreshResourceRequesterSpy implements ResourceRequester {
    public Request receivedRequest;
    @Override
    public Response run(Request request) {
        this.receivedRequest = request;
        return new Response(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}");
    }
}
class LoginRefreshParserSpy extends TokensParser {
    public Response ReceivedResponse;
    @Override
    public Tokens constructFrom(Response response) throws InvalidTokensResponse {
        this.ReceivedResponse = response;
        return new Tokens("f820a39359b7a69436b1c1fdad01a6afbad27f38", "6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8");
    }
}
@RunWith(HierarchicalContextRunner.class)
public class TokensRefresherTest {
    public class GivenARequesterAndParserSpy {
        LoginResourceRequesterSpy requester;
        LoginParserSpy parser;
        @Before
        public void setUp() {
            requester = new LoginResourceRequesterSpy();
            parser = new LoginParserSpy();
        }
        public class WhenLoginRefreshIsExecuted{
            TokensRefresher tokensRefresher;
            Tokens mockToken = new Tokens("66819b0d4c577eab386a58593608cc146882c169", "bc8e9a587d93f0d9fb4e68f06e3aafe46e31f231");
            Tokens tokens;
            @Before
            public void setUp() throws IOException {
                tokensRefresher = new TokensRefresher(requester, parser, mockToken);
                tokens = tokensRefresher.execute();
            }
            @Test
            public void shouldBeDifferentAccessToken() throws IOException {
                assertThat(tokens.access, is(not(mockToken.access)));
            }

            public void shouldBeDifferentRefreshToken() throws IOException {
                assertThat(tokens.refresh, is(not(mockToken.refresh)));
            }
            @Test
            public void shouldReturnRequesterParameters(){
                assertThat(requester.receivedRequest.parameters.containsKey("grant_type"), is(true));
                assertThat(requester.receivedRequest.parameters.get("grant_type"), is("refresh_token"));

                assertThat(requester.receivedRequest.parameters.containsKey("refresh_token"), is(true));
                assertThat(requester.receivedRequest.parameters.get("refresh_token"), is("bc8e9a587d93f0d9fb4e68f06e3aafe46e31f231"));

                assertThat(requester.receivedRequest.parameters.containsKey("client_id"), is(true));
                assertThat(requester.receivedRequest.parameters.get("client_id"), is("NTM0NjQ4NjlhNGY0Yzgz"));

                assertThat(requester.receivedRequest.parameters.containsKey("client_secret"), is(true));
                assertThat(requester.receivedRequest.parameters.get("client_secret"), is("b4c4bb62dac450925d7cf3322a9d4e8fde7bc104"));

            }

        }
    }

}
