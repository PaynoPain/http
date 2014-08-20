package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class JsonListParserTest {
    class ElementParserSpy implements Function<String, String> {
        public ArrayList<String> inputsReceived = new ArrayList<String>();

        @Override
        public String apply(String input) throws IllegalArgumentException {
            inputsReceived.add(input);
            return input;
        }
    }

    ElementParserSpy spy;
    JsonListParser<String> parser;

    @Before
    public void setUp() {
        spy = new ElementParserSpy();
        parser = new JsonListParser<String>(spy);
    }

    public class GivenAValidJson {
        List<String> parsedResult;

        @Before
        public void setUp(){
            parsedResult = parser.apply("[\"Common\",\"Staff\",\"VIP\"]");
        }

        public class ShouldAskTheElementParser {
            @Test
            public void toParseThreeElements() {
                assertThat(spy.inputsReceived.size(), is(3));
            }

            @Test
            public void toParseTheFirstElement() {
                assertThat(spy.inputsReceived.contains("Common"), is(true));
            }

            @Test
            public void toParseTheSecondElement() {
                assertThat(spy.inputsReceived.contains("Staff"), is(true));
            }

            @Test
            public void toParseTheThirdElement() {
                assertThat(spy.inputsReceived.contains("VIP"), is(true));
            }
        }

        @Test
        public void shouldContainAllTheElements() {
            assertThat(parsedResult, is(Arrays.asList("Common", "Staff", "VIP")));
        }
    }

    public class ShouldThrowException {
        @Test(expected = IllegalArgumentException.class)
        public void withAnObject() {
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
    }
}