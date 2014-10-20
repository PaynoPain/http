package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONArray;

import java.util.List;

public class JSONArrayParser<InputElement, OutputElement> implements Function<JSONArray, List<OutputElement>> {
    private static class JSONArrayToListConverter<I, O> implements Function<JSONArray, List<O>> {
        private final Function<Iterable<I>, List<O>> listParser;

        public JSONArrayToListConverter(Function<Iterable<I>, List<O>> listParser) {
            this.listParser = listParser;
        }

        @Override
        public List<O> apply(JSONArray array) throws RuntimeException {
            return listParser.apply(new JSONList<I>(array));
        }
    }

    private final JSONArrayToListConverter<InputElement, OutputElement> parser;

    public JSONArrayParser(Function<InputElement, OutputElement> elementParser) {
        parser = new JSONArrayToListConverter<InputElement, OutputElement>(
                new ListParser<InputElement, OutputElement>(elementParser)
        );
    }

    @Override
    public List<OutputElement> apply(JSONArray array) throws RuntimeException {
        return parser.apply(array);
    }
}

