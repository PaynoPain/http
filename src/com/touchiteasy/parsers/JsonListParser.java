package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonListParser<T> implements Function<String, List<T>> {
    private final Function<String, T> elementParser;

    public JsonListParser(Function<String, T> elementParser) {
        this.elementParser = elementParser;
    }

    @Override
    public List<T> apply(String input) throws IllegalArgumentException {
        try {
            return parseArray(input);
        } catch (JSONException e){
            throw new IllegalArgumentException(e);
        }
    }

    private List<T> parseArray(String input) throws JSONException {
        ArrayList<T> parsedList = new ArrayList<T>();

        JSONArray array;
        array = new JSONArray(input);

        for (int i = 0; i < array.length(); i++){
            final String element = array.get(i).toString();
            final T parsedElement = this.elementParser.apply(element);
            parsedList.add(parsedElement);
        }

        return parsedList;
    }
}
