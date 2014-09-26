package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;
import java.util.List;

public class ValidatorCollection implements ResponseValidator {
    private final List<ResponseValidator> validators;

    public ValidatorCollection(List<ResponseValidator> validators) {
        if (validators.isEmpty())
            throw new IllegalArgumentException("There should be at least one validator!");

        this.validators = validators;
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        return validators.iterator().next().analyse(response);
    }
}
