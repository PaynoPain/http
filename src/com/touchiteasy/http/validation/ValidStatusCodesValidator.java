package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;

public class ValidStatusCodesValidator extends ResponseValidator {
    private final Collection<Integer> validStatusCodes;

    public ValidStatusCodesValidator(Collection<Integer> validStatusCodes){
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
