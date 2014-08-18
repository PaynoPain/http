package com.touchiteasy.http.context;

import java.util.HashMap;
import java.util.Map;

public class AdditionalParametersContext extends Context {
    private final Map<String, String> additionalParameters;

    public AdditionalParametersContext(final Map<String, String> additionalParameters){
        this.additionalParameters = additionalParameters;
    }

    public Map<String, String> getParametersWithContext(Map<String, String> baseParameters){
        Map<String, String> paramsToSend = new HashMap<String, String>();
        paramsToSend.putAll(baseParameters);
        paramsToSend.putAll(additionalParameters);
        return paramsToSend;
    }
}
