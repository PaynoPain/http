package com.touchiteasy.http.context;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.BaseResponse;
import com.touchiteasy.http.RequesterMock;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class ContextRequesterTest {
    public class GivenARequestMockWith2Parameters{
        BaseRequest baseRequest;
        RequesterMock baseRequester;
        ContextRequester contextRequester;

        @Before
        public void setUp(){
            baseRequest = new BaseRequest("url", new LiteralHashMap<String, String>("username", "user", "password", "pass"));
            baseRequester = new RequesterMock();
            baseRequester.responses.add(new BaseResponse(200, ""));
        }

        public class WhenAddingNoParametersToTheContext{
            @Before
            public void setUp(){
                contextRequester = new ContextRequester(baseRequester, new AdditionalParametersContext(new HashMap<String, String>()));
                contextRequester.run(baseRequest);
            }

            @Test
            public void shouldContainOnlyThe2InitialParameters(){
                Map<String, String> expected = new LiteralHashMap<String, String>("username", "user", "password", "pass");
                assertThat(baseRequester.requests.get(0).getParameters(), is(expected));
            }
        }

        public class WhenAdding2MoreParameters{
            @Before
            public void setUp(){
                contextRequester = new ContextRequester(baseRequester, new AdditionalParametersContext(new LiteralHashMap<String, String>("clientId", "client", "clientSecret", "secret")));
                contextRequester.run(baseRequest);
            }

            @Test
            public void shouldContainThe4Parameters(){
                Map<String, String> expected = new LiteralHashMap<String, String>("username", "user", "password", "pass","clientId", "client", "clientSecret", "secret");
                assertThat(baseRequester.requests.get(0).getParameters(), is(expected));
            }

            @Test
            public void shouldHaveTheResourceUnchanged(){
                assertThat(baseRequester.requests.get(0).getResource(), is("url"));
            }
        }
    }
}
