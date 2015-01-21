package com.paynopain.parsers;

import com.paynopain.commons.Function;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class ListParserTest {
    class ElementParserSpy implements Function<String, String> {
        public ArrayList<String> inputsReceived = new ArrayList<String>();

        @Override
        public String apply(String input) throws IllegalArgumentException {
            inputsReceived.add(input);
            return input;
        }
    }

    public class GivenAValidJson {
        ElementParserSpy spy;
        ListParser<String, String> parser;
        List<String> parsedResult;

        @Before
        public void setUp() throws JSONException {
            spy = new ElementParserSpy();
            parser = new ListParser<String, String>(spy);

            parsedResult = parser.apply(new JSONList<String>(new JSONArray("[\"Common\",\"Staff\",\"VIP\"]")));
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

    @Test
    public void ParsingListOfJSONObjectsContent() throws JSONException {
        final ListParser<JSONObject, Integer> listParser = new ListParser<JSONObject, Integer>(
                new Function<JSONObject, Integer>() {
                    @Override
                    public Integer apply(JSONObject object) throws RuntimeException {
                        try {
                            return object.getInt("num");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        final Iterable<JSONObject> input = new JSONList<JSONObject>(new JSONArray("[{\"num\":1},{\"num\":2}]"));

        assertThat(listParser.apply(input), is(Arrays.asList(1, 2)));
    }

    @Test
    public void ParsingListIntegers() throws JSONException {
        final ListParser<Integer, Integer> listParser = new ListParser<Integer, Integer>(new Identity<Integer>());
        final List<Integer> parsed = listParser.apply(new JSONList<Integer>(new JSONArray("[1,2]")));

        assertThat(parsed, is(Arrays.asList(1, 2)));
    }
}