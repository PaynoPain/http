package com.paynopain.parsers;

import com.paynopain.commons.Function;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyListParserTest {
    public static class ToIntSpy implements Function<String, Integer> {
        public List<String> applyCalls = new ArrayList<String>();

        @Override
        public Integer apply(String s) throws RuntimeException {
            applyCalls.add(s);
            return Integer.parseInt(s);
        }
    }

    private ToIntSpy toIntSpy;
    private LazyListParser<String, Integer> lazyListParser;
    private List<Integer> lazyList;

    @Before
    public void setUp() {
        toIntSpy = new ToIntSpy();
        lazyListParser = new LazyListParser<String, Integer>(toIntSpy);
        lazyList = lazyListParser.apply(Arrays.asList("11", "22", "33"));
    }

    @Test
    public void ifNotAccessed_ShouldNotParse() {
        assertThat(toIntSpy.applyCalls.size(), is(0));
    }

    @Test
    public void shouldParseTheElements() {
        assertThat(lazyList, is(Arrays.asList(
                11, 22, 33
        )));
    }

    @Test
    public void whenAccessedToOneElement_ShouldOnlyAskForThatElement() {
        lazyList.get(1);
        assertThat(toIntSpy.applyCalls.size(), is(1));
        assertThat(toIntSpy.applyCalls.get(0), is("22"));
    }
}
