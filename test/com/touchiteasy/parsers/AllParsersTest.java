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
public class AllParsersTest {
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
            forceNullPointerException(input);
            return toReturn;
        }

        private void forceNullPointerException(DS input) {
            input.toString();
        }
    }

    class FailingParser implements Function<DS, O> {
        @Override
        public O apply(DS input) throws IllegalArgumentException {
            throw new IllegalArgumentException("The failing parser fails to parse everything!");
        }
    }

    AllParsers<DS, O> parser;

    public class GivenNoParsers {
        @Test
        public void shouldReturnEmptyList(){
            parser = new AllParsers<DS, O>(new ArrayList<Function<DS, O>>());

            final List<O> parsedObjects = parser.apply(new DataStructure());

            assertThat(parsedObjects.size(), is(0));
        }
    }

    public class GivenTwoSuccessfulParsers {
        OutputObject parsed1;
        OutputObject parsed2;
        List<O> parsedObjects;

        @Before
        public void setUp(){
            parsed1 = new OutputObject();
            parsed2 = new OutputObject();

            parser = new AllParsers<DS, O>(Arrays.asList(
                    new SuccessfulParser(parsed1),
                    new SuccessfulParser(parsed2)
            ));

            parsedObjects = parser.apply(new DataStructure());
        }

        @Test
        public void shouldParseAListWith2Object(){
            assertThat(parsedObjects.size(), is(2));
        }

        @Test
        public void shouldReturnTheFirstParsedObject(){
            assertThat(parsedObjects.contains(parsed1), is(true));
        }

        @Test
        public void shouldReturnTheSecondParsedObject(){
            assertThat(parsedObjects.contains(parsed2), is(true));
        }
    }

    public class GivenOneFailingParser {
        @Test(expected = IllegalArgumentException.class)
        public void shouldThrowException(){
            parser = new AllParsers<DS, O>(Arrays.asList(
                    new SuccessfulParser(new OutputObject()),
                    new SuccessfulParser(new OutputObject()),
                    new FailingParser()
            ));

            parser.apply(new DataStructure());
        }
    }
}