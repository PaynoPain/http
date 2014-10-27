package com.touchiteasy.http.context;

import com.touchiteasy.commons.LiteralHashMap;
import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContextRequestTest {
    BaseRequest base;
    Request requestWithResourceContext;

    @Before
    public void setUp(){
        base = new BaseRequest(
                "::base_resource::",
                new LiteralHashMap<String, String>(
                        "::base_key::", "::base_value::"
                )
        );
        requestWithResourceContext = new ContextRequest(base, new Context() {
            @Override
            public String getResourceWithContext(String baseResource) {
                return "::context_resource::" + baseResource;
            }

            @Override
            public Map<String, String> getParametersWithContext(Map<String, String> baseParameters) {
                final HashMap<String, String> parametersWithContext = new HashMap<String, String>();

                parametersWithContext.putAll(baseParameters);
                parametersWithContext.put("::context_key::", "::context_value::");

                return parametersWithContext;
            }
        });
    }

    @Test
    public void WhenGettingTheResource_ShouldHaveAddedTheContext() {
        assertThat(requestWithResourceContext.getResource(), is("::context_resource::::base_resource::"));
    }

    @Test
    public void WhenGettingTheParameters_ShouldHaveAddedTheContext() {
        assertThat(requestWithResourceContext.getParameters().size(), is(2));
        assertThat(requestWithResourceContext.getParameters().containsKey("::base_key::"), is(true));
        assertThat(requestWithResourceContext.getParameters().get("::base_key::"), is("::base_value::"));
        assertThat(requestWithResourceContext.getParameters().containsKey("::context_key::"), is(true));
        assertThat(requestWithResourceContext.getParameters().get("::context_key::"), is("::context_value::"));
    }
}
