package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(HierarchicalContextRunner.class)
public class AllSuitableParsersTest {
    interface DS {}
    interface O {}

    class DataStructure implements DS {}
    class OutputObject implements O {}

    class SuccessfulParser implements Function<DS, O> {
        private O toReturn;

        public SuccessfulParser(O objectToReturnWhenParsing){
            toReturn = objectToReturnWhenParsing;
        }

        @Override
        public O apply(DS input) throws IllegalArgumentException {
            return toReturn;
        }
    }

    class FailingParser implements Function<DS, O> {
        @Override
        public O apply(DS input) throws IllegalArgumentException {
            throw new IllegalArgumentException("The failing parser fails to parse everything!");
        }
    }

    AllSuitableParsers<DS, O> parser;

    public class GivenNoParsers {
        @Test(expected = IllegalArgumentException.class)
        public void shouldThrowException(){
            new AllSuitableParsers<DS, O>(
                    new ArrayList<Function<DS, O>>()
            );
        }
    }

    public class GivenOnlyASuccessfulParser {
        O parserReturnValue;

        @Before
        public void setUp() {
            parserReturnValue = new OutputObject();
            parser = new AllSuitableParsers<DS, O>(Arrays.asList(
                    new SuccessfulParser(parserReturnValue)
            ));
        }

        public class WhenParsing {
            List<O> parsedValues;

            @Before
            public void setUp() {
                parsedValues = parser.apply(new DataStructure());
            }

            @Test
            public void shouldReturnAListWithOneObject (){
                assertThat(parsedValues.size(), is(1));
            }

            @Test
            public void shouldReturnAListWithTheParsedObject (){
                assertThat(parsedValues.get(0), is(parserReturnValue));
            }
        }
    }

    public class GivenOnlyFailingParsers {
        @Before
        public void setUp() {
            parser = new AllSuitableParsers<DS, O>(Arrays.asList(
                    new FailingParser(),
                    new FailingParser(),
                    new FailingParser()
            ));
        }

        @Test(expected = IllegalArgumentException.class)
        public void whenParsing_ShouldThrowException (){
            parser.apply(new DataStructure());
        }
    }

    public class GivenAFailingParserAndASuccessfulParser {
        O parserReturnValue;

        @Before
        public void setUp() {
            parserReturnValue = new OutputObject();
            parser = new AllSuitableParsers<DS, O>(Arrays.asList(
                    new FailingParser(),
                    new SuccessfulParser(parserReturnValue)
            ));
        }

        public class WhenParsing {
            List<O> parsedValues;

            @Before
            public void setUp() {
                parsedValues = parser.apply(new DataStructure());
            }

            @Test
            public void shouldReturnAListWithOneObject (){
                assertThat(parsedValues.size(), is(1));
            }

            @Test
            public void shouldReturnAListWithTheParsedObject (){
                assertThat(parsedValues.get(0), is(parserReturnValue));
            }
        }
    }
}