package com.paynopain.parsers;

import com.paynopain.commons.Function;

import java.util.*;

public class LazyListParser<Input, Output> implements Function<List<Input>, List<Output>> {
    private static class ParsedLazilyList<I, O> extends AbstractList<O> {
        private final List<I> input;
        private final Function<I, O> elementParser;

        public ParsedLazilyList(List<I> input, Function<I, O> elementParser) {
            this.input = input;
            this.elementParser = elementParser;
        }

        @Override
        public O get(int index) {
            return elementParser.apply(input.get(index));
        }

        @Override
        public int size() {
            return input.size();
        }
    }
    
    private final Function<Input, Output> elementParser;

    public LazyListParser(Function<Input, Output> elementParser) {
        this.elementParser = elementParser;
    }

    @Override
    public List<Output> apply(List<Input> inputs) throws RuntimeException {
        return new ParsedLazilyList<Input, Output>(inputs, elementParser);
    }
}
