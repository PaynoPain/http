package com.paynopain.parsers;

import com.paynopain.commons.Function;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class JSONObjectUnwrapperTest {
    class UnwrappedParserSpy implements Function<String, String> {
        public ArrayList<String> inputsReceived = new ArrayList<String>();

        @Override
        public String apply(String input) throws IllegalArgumentException {
            inputsReceived.add(input);
            return "::parsed_content::";
        }
    }

    UnwrappedParserSpy unwrappedParser;
    JSONObjectUnwrapper<String, String> parser;

    @Before
    public void setUp() {
        unwrappedParser = new UnwrappedParserSpy();
        parser = new JSONObjectUnwrapper<String, String>("key", unwrappedParser);
    }

    public class GivenAValidJson {
        String parsedResult;

        @Before
        public void setUp() throws JSONException {
            parsedResult = parser.apply(new JSONObject("{key: \"::content::\"}"));
        }

        @Test
        public void ShouldAskTheUnwrappedParserToParseTheContent() {
            assertThat(unwrappedParser.inputsReceived.size(), is(1));
            assertThat(unwrappedParser.inputsReceived.get(0), is("::content::"));
        }

        @Test
        public void ShouldReturnTheParsedContentByTheGivenUnwrappedParser() {
            assertThat(parsedResult, is("::parsed_content::"));
        }
    }

    public class ShouldThrowException {
        @Test(expected = IllegalArgumentException.class)
        public void withAnObjectWithoutTheKey() throws JSONException {
            parser.apply(new JSONObject("{}"));
        }
    }

    @Test
    public void integrateWithAnotherParser() throws JSONException {
        final Function<String, Integer> parseInt = new Function<String, Integer>() {
            @Override
            public Integer apply(String num) throws RuntimeException {
                return Integer.parseInt(num);
            }
        };
        final JSONObjectUnwrapper<String, Integer> parser = new JSONObjectUnwrapper<String, Integer>("key", parseInt);

        assertThat(parser.apply(new JSONObject("{\"key\":\"123\"}")), is(123));
    }
}