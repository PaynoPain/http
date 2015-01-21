package com.paynopain.http.validation;

import com.paynopain.http.Response;

import java.util.Collection;

public interface ResponseValidator {
    public Collection<InvalidationCause> analyse(Response response);
}
