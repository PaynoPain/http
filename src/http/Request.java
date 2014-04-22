package http;

import java.util.HashMap;
import java.util.Map;

public class Request {
    public final String resource;
    public final Map<String, String> parameters;

    public Request(String resource, Map<String, String> parameters) {
        checkField(resource, "resource");
        checkField(parameters, "parameters");

        this.resource = resource;
        this.parameters = parameters;
    }

    public Request(String resource) {
        this(resource, new HashMap<String, String>());
    }

    public Request(String resource, String ... keyValuePairs) {
        this(resource, generateMap(keyValuePairs));
    }

    private static void checkField(Object obj, String fieldName) {
        if (obj == null){
            throw new IllegalArgumentException("The " + fieldName + " argument is required!");
        }
    }

    private static Map<String, String> generateMap(String ... keyValuePairs){
        if (keyValuePairs.length % 2 != 0){
            throw new IllegalArgumentException("The number of key-value pair arguments should be even!");
        }
        Map<String, String> params = new HashMap<String, String>();

        for (int i = 0; i < keyValuePairs.length; i += 2){
            params.put(keyValuePairs[i], keyValuePairs[i+1]);
        }

        return params;
    }
}
