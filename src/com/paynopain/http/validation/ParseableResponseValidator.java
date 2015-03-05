package com.paynopain.http.validation;

import com.paynopain.commons.Function;
import com.paynopain.http.Response;

import java.util.Arrays;
import java.util.Collection;

public class ParseableResponseValidator<ParsedOutput> implements ResponseValidator {
    private final Function<Response, ParsedOutput> parser;

    public ParseableResponseValidator(Function<Response, ParsedOutput> parser) {
        this.parser = parser;
    }

    @Override
    public Collection<InvalidationCause> analyse(Response response) {
        try {
            parser.apply(response);
        } catch (RuntimeException e) {
            return Arrays.asList(new InvalidationCause("the body can't be parsed: " + e.getMessage()));
        }

        return Arrays.asList();
    }
}
