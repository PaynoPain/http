package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import org.json.JSONObject;

public class JSONObjectUnwrapper<Unwrapped, Parsed> implements Function<JSONObject, Parsed> {
    private final String attribute;
    private final Function<Unwrapped, Parsed> unwrappedParser;

    public JSONObjectUnwrapper(String attribute, Function<Unwrapped, Parsed> unwrappedParser) {
        this.attribute = attribute;
        this.unwrappedParser = unwrappedParser;
    }

    @Override
    public Parsed apply(JSONObject input) throws RuntimeException {
        return unwrap(input);
    }

    private Parsed unwrap(JSONObject input) {
        final Unwrapped content;

        try {
            content = (Unwrapped) input.get(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return unwrappedParser.apply(content);
    }
}
