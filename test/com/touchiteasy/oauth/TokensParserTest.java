package com.touchiteasy.oauth;

import com.touchiteasy.http.BaseResponse;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import com.touchiteasy.http.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.json.JSONException;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class TokensParserTest {
    public class GivenAServerMockWhitPredefinedBodyResponse {
        TokensParser tokensParser;

        @Before
        public void setUp() throws IOException {
            tokensParser = new TokensParser();
        }

        public class WhenResponseCodeIs200 {
            Tokens tokensToAssert;
            Response mock;
            @Before
            public void setUp() throws JSONException {
                mock = new BaseResponse(200, "{\"access_token\":\"f820a39359b7a69436b1c1fdad01a6afbad27f38\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8\"}");
                tokensToAssert = new Tokens("f820a39359b7a69436b1c1fdad01a6afbad27f38", "6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8");
            }

            @Test
            public void ShouldReturnAccessToken() throws InvalidTokensResponse {
                MatcherAssert.assertThat(tokensParser.constructFrom(mock).access, CoreMatchers.is(tokensToAssert.access));
            }
            @Test
            public void ShouldReturnRefreshToken() throws InvalidTokensResponse {
                MatcherAssert.assertThat(tokensParser.constructFrom(mock).refresh, CoreMatchers.is(tokensToAssert.refresh));
            }
        }

        public class WhenResponseCodeIsNot200 {
            Response mock;
            @Before
            public void setUp() {
                mock = new BaseResponse(400, "{\"error\":\"invalid_client\",\"error_description\":\"Client id was not found in the headers or body\"}");
            }

            @Test(expected = InvalidTokensResponse.class)
            public void ShouldReturnBadRequestException() throws InvalidTokensResponse {
                tokensParser.constructFrom(mock);
            }
        }
    }
}
