package com.paynopain.parsers;

import com.paynopain.commons.Function;
import com.paynopain.http.BaseRequest;
import com.paynopain.http.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonRequestParser implements Function<String, Request>{
    @Override
    public Request apply(String jsonRequest) throws RuntimeException {
        try {
            final JSONObject json = new JSONObject(jsonRequest);

            final String resource = json.getString("resource");
            final Map<String, String> parameters = parseParameters(json.getJSONObject("parameters"));

            return new BaseRequest(
                    resource,
                    parameters
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> parseParameters(JSONObject json) throws JSONException {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        final Iterator keys = json.keys();

        while (keys.hasNext()){
            final String key = (String) keys.next();
            parameters.put(key, json.getString(key));
        }

        return parameters;
    }
}
