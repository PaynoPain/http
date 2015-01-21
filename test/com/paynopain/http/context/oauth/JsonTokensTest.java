package com.paynopain.http.context.oauth;

import com.paynopain.http.BaseResponse;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class JsonTokensTest {
    public class GivenAServerMockWhitPredefinedBodyResponse {
        Tokens jsonTokens;

        public class WhenResponseCodeIs200 {
            final String RESPONDED_ACCESS_TOKEN = "f820a39359b7a69436b1c1fdad01a6afbad27f38",
                         RESPONDED_REFRESH_TOKEN = "6b28235f4a23f5a1c2feb1bf7bd5e9c674f3d7a8";

            @Before
            public void setUp() throws IOException {
                BaseResponse response = new BaseResponse(
                        200,
                        "{\"access_token\":\"" + RESPONDED_ACCESS_TOKEN + "\",\"expires_in\":3600,\"token_type\":\"bearer\",\"scope\":null,\"refresh_token\":\"" + RESPONDED_REFRESH_TOKEN + "\"}"
                );

                jsonTokens = new JsonTokens(response);
            }

            @Test
            public void ShouldReturnAccessToken() throws InvalidTokensResponse {
                assertThat(jsonTokens.getAccess(), is(RESPONDED_ACCESS_TOKEN));
            }
            @Test
            public void ShouldReturnRefreshToken() throws InvalidTokensResponse {
                assertThat(jsonTokens.getRefresh(), is(RESPONDED_REFRESH_TOKEN));
            }
        }

        public class WhenResponseCodeIsNot200 {
            BaseResponse response;

            @Before
            public void setUp() throws IOException {
                response = new BaseResponse(
                        400,
                        "{\"error\":\"invalid_client\",\"error_description\":\"Client id was not found in the headers or body\"}"
                );
            }

            @Test(expected = InvalidTokensResponse.class)
            public void ShouldReturnBadRequestException() throws InvalidTokensResponse {
                jsonTokens = new JsonTokens(response);
            }
        }
    }
}
