package com.paynopain.parsers;

import com.paynopain.commons.Function;
import com.paynopain.http.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponseParser<ParsedOutput> implements Function<Response, ParsedOutput> {
    private final Function<JSONObject, ParsedOutput> bodyParser;

    public JSONResponseParser(Function<JSONObject, ParsedOutput> bodyParser){
        this.bodyParser = bodyParser;
    }

    @Override
    public ParsedOutput apply(Response response) throws RuntimeException {
        try {
            final JSONObject jsonBody = new JSONObject(response.getBody());
            return bodyParser.apply(jsonBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
