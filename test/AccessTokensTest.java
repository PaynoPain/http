import de.bechte.junit.runners.context.HierarchicalContextRunner;
import http.Request;
import http.ResourceRequester;
import http.Response;
import org.json.JSONException;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;



import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class OauthServerMock implements ResourceRequester{
    public int responseCode;
    public String responseBody;

    OauthServerMock(int responseCode, String responseBody){
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }
    @Override
    public Response run(Request request) {
        return new Response(responseCode, responseBody);
    }
}
@RunWith(HierarchicalContextRunner.class)
public class AccessTokensTest {
    TokensGetter tokensGetter;
    OauthServerMock mock;


    @Before
    public void setUp() throws IOException {
        tokensGetter = new TokensGetter();
    }

    public class GivenAServerMockWhitPredefinedBodyResponse {
        TokensGetter tokensGetter;

        @Before
        public void setUp() throws IOException {
            tokensGetter = new TokensGetter();
        }

        public class WhenResponseCodeIs200 {
            Tokens tokensToAssert;
            @Before
            public void setUp() throws JSONException {
                mock = new OauthServerMock(200, getResponseBody());
                tokensToAssert = new Tokens("f820a39359b7a69436b1c1fdad01a6afbad27f38", "6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8");
            }

            @Test
            public void ShouldReturnAccessToken() throws JSONException, BadRequestException {
                assertThat(tokensGetter.getAccessToken(runMockWithoutRequest()).access, is(tokensToAssert.access));
            }
            @Test
            public void ShouldReturnRefeshToken() throws JSONException, BadRequestException {
                assertThat(tokensGetter.getAccessToken(runMockWithoutRequest()).refresh, is(tokensToAssert.refresh));
            }
            private String getResponseBody(){
                return "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}";
            }
        }

        public class WhenResponseCodeIsNot200 {
            @Before
            public void setUp() {
                mock = new OauthServerMock(400, getResponseBody());
            }

            @Test(expected = BadRequestException.class)
            public void ShouldReturnBadRequesExeption() throws JSONException, BadRequestException {
                tokensGetter.getAccessToken(runMockWithoutRequest());
            }
            private String getResponseBody(){
                return "{\"error\":\"invalid_client\",\"error_description\":\"Client id was not found in the headers or body\"}";
            }
        }
    }
    private Response runMockWithoutRequest() {
        return mock.run(new Request(new String(), new HashMap<String, String>()));
    }
}
