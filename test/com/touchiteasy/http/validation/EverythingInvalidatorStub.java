package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Arrays;
import java.util.Collection;

public class EverythingInvalidatorStub implements ResponseValidator {
    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        return Arrays.asList(new InvalidationCause("everything is invalid"));
    }
}
