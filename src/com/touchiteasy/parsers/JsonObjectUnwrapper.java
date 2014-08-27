package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectUnwrapper<T> implements Function<String, T> {
    private final String attribute;
    private final Function<String, T> unwrappedParser;

    public JsonObjectUnwrapper(String attribute, Function<String, T> unwrappedParser) {
        this.attribute = attribute;
        this.unwrappedParser = unwrappedParser;
    }

    @Override
    public T apply(String input) throws RuntimeException {
        return unwrap(input);
    }

    private T unwrap(String input) {
        final Object content;

        try {
            content = new JSONObject(input).get(attribute);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }

        final String unwrapped = content.toString();
        return unwrappedParser.apply(unwrapped);
    }
}
