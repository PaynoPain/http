package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;

public class ResponseValidatorStub implements ResponseValidator {
    private final Collection<InvalidationCause> returnValue;

    public ResponseValidatorStub(Collection<InvalidationCause> returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        return returnValue;
    }
}
