package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Arrays;
import java.util.Collection;

public class EverythingValidatorStub implements ResponseValidator {
    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        return Arrays.asList();
    }
}
