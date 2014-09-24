package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;

public class ValidStatusCodesValidator extends ResponseValidator {
    private final Collection<Integer> validStatusCodes;

    public ValidStatusCodesValidator(Collection<Integer> validStatusCodes){
        this.validStatusCodes = validStatusCodes;
    }

    @Override
    protected ValidationResult analyse(Response response) {
        if (validStatusCodes.contains(response.getStatusCode()))
            return ValidationResult.valid();
        else
            return ValidationResult.invalid(
                    String.format("the only valid status codes are %s", validStatusCodes.toString()));
    }
}
