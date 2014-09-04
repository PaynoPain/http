package com.touchiteasy.parsers;

import com.touchiteasy.http.Request;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonRequestParserTest {
    private JsonRequestParser parser;

    @Before
    public void setUp() {
        parser = new JsonRequestParser();
    }

    @Test
    public void GivenInputString_ShouldParseIt() {
        Request request = parser.apply(
                "{\n" +
                "    \"resource\": \"::resource::\",\n" +
                "    \"parameters\": {\n" +
                "        \"::key1::\": \"::value1::\",\n" +
                "        \"::key2::\": \"::value2::\"\n" +
                "    }\n" +
                "}"
        );
        assertEquals(request.getResource(), "::resource::");
        assertEquals(request.getParameters().size(), 2);
        assertEquals(request.getParameters().get("::key1::"), "::value1::");
        assertEquals(request.getParameters().get("::key2::"), "::value2::");
    }

    @Test (expected = RuntimeException.class)
    public void GivenInputWithoutResource_ThrowsException() {
        parser.apply(
                "{\n" +
                "    \"parameters\": {\n" +
                "        \"::key1::\": \"::value1::\",\n" +
                "        \"::key2::\": \"::value2::\"\n" +
                "    }\n" +
                "}"
        );
    }

    @Test (expected = RuntimeException.class)
    public void GivenInputWithoutParameters_ThrowsException() {
        parser.apply(
                "{\n" +
                "    \"resource\": \"::resource::\"\n" +
                "}"
        );
    }
}
