package com.touchiteasy.parsers;

import com.touchiteasy.commons.Function;
import com.touchiteasy.http.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JsonRequestComposer implements Function<Request, String>{
    @Override
    public String apply(Request request) throws RuntimeException {
        try {
            final JSONObject json = new JSONObject();

            json.put("resource", request.getResource());

            final JSONObject parameters = new JSONObject();
            for (Map.Entry<String, String> entry : request.getParameters().entrySet()) {
                parameters.put(entry.getKey(), entry.getValue());
            }

            json.put("parameters", parameters);

            return json.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
