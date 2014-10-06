package com.touchiteasy.http.validation;

import com.touchiteasy.http.IdentifiableResponse;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.Response;

import java.util.Collection;

public class ResponseChecker {
    private final ResponseValidator validator;

    public ResponseChecker(ResponseValidator validator){
        this.validator = validator;
    }

    public void check(Response response) throws InvalidResponseException {
        final Collection<InvalidationCause> invalidationCauses = validator.analyse(response);
        if (!invalidationCauses.isEmpty())
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    getMessage(invalidationCauses),
                    new IdentifiableResponse(response).toString()
            ));
    }

    private String getMessage(Collection<InvalidationCause> invalidationCauses) {
        final String separator = ", ";
        String message = "";

        for (InvalidationCause cause : invalidationCauses){
            message += cause.causeDescription + separator;
        }

        return message.substring(0, message.length() - separator.length());
    }
}
