package com.touchiteasy.http;

import java.util.List;

public class ValidStatusCodesValidator implements ResponseValidatingRequester.ResponseValidator {
    private final List<Integer> validStatusCodes;

    public ValidStatusCodesValidator(List<Integer> validStatusCodes){
        this.validStatusCodes = validStatusCodes;
    }

    @Override
    public boolean isValid(Response response) {
        return validStatusCodes.contains(response.getStatusCode());
    }

    @Override
    public String getCauseDescription() {
        return String.format("the only valid status codes are %s", validStatusCodes.toString());
    }
}
