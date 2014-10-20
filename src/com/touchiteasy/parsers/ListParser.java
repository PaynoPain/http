package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListParser<Input, Output> implements Function<Iterable<Input>, List<Output>> {
    private final Function<Input, Output> elementParser;

    public ListParser(Function<Input, Output> elementParser) {
        this.elementParser = elementParser;
    }

    @Override
    public List<Output> apply(Iterable<Input> input) throws IllegalArgumentException {
        try {
            return parseArray(input);
        } catch (JSONException e){
            throw new IllegalArgumentException(e);
        }
    }

    private List<Output> parseArray(Iterable<Input> input) throws JSONException {
        ArrayList<Output> parsedList = new ArrayList<Output>();

        for (Input i : input){
            final Output parsedElement = this.elementParser.apply(i);
            parsedList.add(parsedElement);
        }

        return parsedList;
    }
}
