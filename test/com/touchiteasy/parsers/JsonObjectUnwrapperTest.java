package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class JsonObjectUnwrapperTest {
    class UnwrappedParserSpy implements Function<String, String> {
        public ArrayList<String> inputsReceived = new ArrayList<String>();

        @Override
        public String apply(String input) throws IllegalArgumentException {
            inputsReceived.add(input);
            return "::parsed_content::";
        }
    }

    UnwrappedParserSpy unwrappedParser;
    JsonObjectUnwrapper<String> parser;

    @Before
    public void setUp() {
        unwrappedParser = new UnwrappedParserSpy();
        parser = new JsonObjectUnwrapper<String>("key", unwrappedParser);
    }

    public class GivenAValidJson {
        String parsedResult;

        @Before
        public void setUp(){
            parsedResult = parser.apply("{key: \"::content::\"}");
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
        public void withAnObjectWithoutTheKey() {
            parser.apply("{}");
        }

        @Test(expected = IllegalArgumentException.class)
        public void withAString() {
            parser.apply("foo");
        }

        @Test(expected = IllegalArgumentException.class)
        public void withANumber() {
            parser.apply("5");
        }

        @Test(expected = IllegalArgumentException.class)
        public void withAnArray() {
            parser.apply("[5]");
        }
    }
}