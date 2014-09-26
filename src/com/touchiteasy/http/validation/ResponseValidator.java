package com.touchiteasy.http.validation;

import com.touchiteasy.http.Response;

import java.util.Collection;

public interface ResponseValidator {
    public Collection<InvalidationCause> analyse(Response response);
}
