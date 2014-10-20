package com.touchiteasy.parsers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.AbstractList;

public class JSONList<Element> extends AbstractList<Element> {
    private final JSONArray array;

    public JSONList(JSONArray array) {
        this.array = array;
    }

    @Override
    public Element get(int index) {
        try {
            return (Element) array.get(index);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return array.length();
    }
}