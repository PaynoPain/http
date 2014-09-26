package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Arrays;
import java.util.Collection;

public class ValidStatusCodesValidator implements ResponseValidator {
    private final Collection<Integer> validStatusCodes;

    public ValidStatusCodesValidator(Collection<Integer> validStatusCodes){
        this.validStatusCodes = validStatusCodes;
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        if (validStatusCodes.contains(response.getStatusCode()))
            return Arrays.asList();
        else
            return Arrays.asList(new InvalidationCause(
                    String.format("the only valid status codes are %s", validStatusCodes.toString())
            ));
    }
}
