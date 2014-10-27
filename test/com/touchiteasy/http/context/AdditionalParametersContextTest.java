package com.touchiteasy.http.context;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.Request;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class AdditionalParametersContextTest {
    public class GivenARequestMockWith2Parameters{
        BaseRequest mock;
        @Before
        public void setUp(){
            mock = new BaseRequest("url", new LiteralHashMap<String, String>("username", "user", "password", "pass"));
        }
        public class WhenAddingNoParametersToTheContext{
            Request paramsMerger;
            @Before
            public void setUp(){
                paramsMerger = new ContextRequest(mock, new AdditionalParametersContext(new HashMap<String, String>()));
            }
            @Test
            public void shouldContainOnlyThe2InitialParameters(){
                Map<String, String> expected = new LiteralHashMap<String, String>("username", "user", "password", "pass");
                assertThat(paramsMerger.getParameters(), is(expected));
            }
        }
        public class WhenAdding2MoreParameters{
            Request paramsMerger;
            @Before
            public void setUp(){
                paramsMerger = new ContextRequest(mock, new AdditionalParametersContext(new LiteralHashMap<String, String>("clientId", "client", "clientSecret", "secret")));
            }
            @Test
            public void shouldContainThe4Parameters(){
                Map<String, String> expected = new LiteralHashMap<String, String>("username", "user", "password", "pass","clientId", "client", "clientSecret", "secret");
                assertThat(paramsMerger.getParameters(), is(expected));
            }
            @Test
            public void shouldHaveTheResourceUnchanged(){
                assertThat(paramsMerger.getResource(), is("url"));
            }
        }
    }
}
