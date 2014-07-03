package com.touchiteasy.http;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.oauth.ResourceRequest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class RequesterWithContextTest {
    public class GivenARequestMockWith2Parameters{
        BaseRequest baseRequest;
        RequesterMock baseRequester;
        RequesterWithContext requesterWithContext;

        @Before
        public void setUp(){
            baseRequest = new BaseRequest("url", new LiteralStringsMap("username", "user", "password", "pass"));
            baseRequester = new RequesterMock();
            baseRequester.responses.add(new BaseResponse(200, ""));
        }

        public class WhenAddingNoParametersToTheContext{
            @Before
            public void setUp(){
                requesterWithContext = new RequesterWithContext(baseRequester, new HashMap<String, String>());
                requesterWithContext.run(baseRequest);
            }

            @Test
            public void shouldContainOnlyThe2InitialParameters(){
                Map<String, String> expected = new LiteralStringsMap("username", "user", "password", "pass");
                assertThat(baseRequester.requests.get(0).getParameters(), is(expected));
            }
        }

        public class WhenAdding2MoreParameters{
            @Before
            public void setUp(){
                requesterWithContext = new RequesterWithContext(baseRequester, new LiteralStringsMap("clientId", "client", "clientSecret", "secret"));
                requesterWithContext.run(baseRequest);
            }

            @Test
            public void shouldContainThe4Parameters(){
                Map<String, String> expected = new LiteralStringsMap("username", "user", "password", "pass","clientId", "client", "clientSecret", "secret");
                assertThat(baseRequester.requests.get(0).getParameters(), is(expected));
            }

            @Test
            public void shouldHaveTheResourceUnchanged(){
                assertThat(baseRequester.requests.get(0).getResource(), is("url"));
            }
        }
    }
}
