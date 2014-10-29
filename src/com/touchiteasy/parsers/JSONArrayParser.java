package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONArray;

import java.util.List;

public class JSONArrayParser<InputElement, OutputElement> implements Function<JSONArray, List<OutputElement>> {
    private final ListParser<InputElement, OutputElement> listParser;

    public JSONArrayParser(Function<InputElement, OutputElement> elementParser) {
        listParser = new ListParser<InputElement, OutputElement>(elementParser);
    }

    @Override
    public List<OutputElement> apply(JSONArray array) throws RuntimeException {
        return listParser.apply(new JSONList<InputElement>(array));
    }
}