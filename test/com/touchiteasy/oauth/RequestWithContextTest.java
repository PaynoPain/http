package com.touchiteasy.oauth;

import com.touchiteasy.LiteralStringsMap;
import com.touchiteasy.http.BaseRequest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class RequestWithContextTest {
    public class GivenARequestMockWith2Parameters{
        BaseRequest mock;
        @Before
        public void setUp(){
            mock = new BaseRequest("url", new LiteralStringsMap("username", "user", "password", "pass"));
        }
        public class WhenAddingNoParametersToTheContext{
            RequestWithContext paramsMerger;
            @Before
            public void setUp(){
                paramsMerger = new RequestWithContext(mock, new HashMap<String, String>());
            }
            @Test
            public void shouldContainOnlyThe2InitialParameters(){
                Map<String, String> expected = new LiteralStringsMap("username", "user", "password", "pass");
                assertThat(paramsMerger.getParameters(), is(expected));
            }
        }
        public class WhenAdding2MoreParameters{
            RequestWithContext paramsMerger;
            @Before
            public void setUp(){
                paramsMerger = new RequestWithContext(mock,  new LiteralStringsMap("clientId", "client", "clientSecret", "secret"));
            }
            @Test
            public void shouldContainThe4Parameters(){
                Map<String, String> expected = new LiteralStringsMap("username", "user", "password", "pass","clientId", "client", "clientSecret", "secret");
                assertThat(paramsMerger.getParameters(), is(expected));
            }
            @Test
            public void shouldHaveTheResourceUnchanged(){
                assertThat(paramsMerger.getResource(), is("url"));
            }
        }
    }
}
