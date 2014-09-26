package com.touchiteasy.http.validation;

import com.touchiteasy.http.IdentifiableResponse;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

import java.util.Collection;

public class ResponseValidatingRequester implements ResourceRequester {
    private final ResourceRequester baseRequester;
    private final ResponseValidator validator;

    public ResponseValidatingRequester(ResourceRequester baseRequester, ResponseValidator validator) {
        this.baseRequester = baseRequester;
        this.validator = validator;
    }

    @Override
    public Response run(Request request) {
        Response response = baseRequester.run(request);

        final Collection<InvalidationCause> invalidationCauses = validator.analyse(response);
        if (!invalidationCauses.isEmpty())
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    getMessage(invalidationCauses),
                    new IdentifiableResponse(response).toString()
            ));

        return response;
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
