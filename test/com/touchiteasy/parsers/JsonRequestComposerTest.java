package com.touchiteasy.parsers;

import com.touchiteasy.commons.LiteralStringsMap;
import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.IdentifiableRequest;
import com.touchiteasy.http.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonRequestComposerTest {
    private JsonRequestComposer composer;

    @Before
    public void setUp() {
        composer = new JsonRequestComposer();
    }

    @Test
    public void GivenARequest_ShouldComposeItCorrectly() {
        final Request initial = new IdentifiableRequest(new BaseRequest(
                "::resource::",
                new LiteralStringsMap(
                        "::key1::", "::value1::",
                        "::key2::", "::value2::"
                )
        ));

        final String composed = composer.apply(initial);

        final JsonRequestParser parser = new JsonRequestParser();
        final Request parsed = parser.apply(composed);

        assertEquals(parsed.getResource(), "::resource::");
        assertEquals(parsed.getParameters().size(), 2);
        assertEquals(parsed.getParameters().get("::key1::"), "::value1::");
        assertEquals(parsed.getParameters().get("::key2::"), "::value2::");
    }

    @Test
    public void GivenARequestWithAnotherJsonInsideAValue_ShouldComposeItCorrectly() throws JSONException {
        final JSONObject jsonValue = new JSONObject();
        jsonValue.put("::key::", "::value::");

        final Request initial = new IdentifiableRequest(new BaseRequest(
                "::resource::",
                new LiteralStringsMap(
                        "::json_value::", jsonValue.toString()
                )
        ));

        final String composed = composer.apply(initial);

        final JsonRequestParser parser = new JsonRequestParser();
        final Request parsed = parser.apply(composed);

        assertEquals(parsed.getResource(), "::resource::");
        assertEquals(parsed.getParameters().size(), 1);
        assertEquals(parsed.getParameters().get("::json_value::"), jsonValue.toString());
    }
}
