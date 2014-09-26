package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;

public class ValidatorCollection implements ResponseValidator {
    public ValidatorCollection() {
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        return null;
    }
}
