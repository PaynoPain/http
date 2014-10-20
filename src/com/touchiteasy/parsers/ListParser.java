package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListParser<I, O> implements Function<Iterable<I>, List<O>> {
    private final Function<I, O> elementParser;

    public ListParser(Function<I, O> elementParser) {
        this.elementParser = elementParser;
    }

    @Override
    public List<O> apply(Iterable<I> input) throws IllegalArgumentException {
        try {
            return parseArray(input);
        } catch (JSONException e){
            throw new IllegalArgumentException(e);
        }
    }

    private List<O> parseArray(Iterable<I> input) throws JSONException {
        ArrayList<O> parsedList = new ArrayList<O>();

        for (I i : input){
            final O parsedElement = this.elementParser.apply(i);
            parsedList.add(parsedElement);
        }

        return parsedList;
    }
}
