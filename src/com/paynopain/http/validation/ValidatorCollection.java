package com.paynopain.http.validation;

import com.paynopain.http.Response;

import java.util.ArrayList;
import java.util.Collection;

public class ValidatorCollection implements ResponseValidator {
    private final Collection<ResponseValidator> validators;

    public ValidatorCollection(Collection<ResponseValidator> validators) {
        if (validators.isEmpty())
            throw new IllegalArgumentException("There should be at least one validator!");

        this.validators = validators;
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        final ArrayList<InvalidationCause> causes = new ArrayList<InvalidationCause>();

        for (ResponseValidator validator : validators)
            causes.addAll(validator.analyse(response));

        return causes;
    }
}
