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

        final Collection<InvalidationCause> invalidations = validator.analyse(response);
        if (!invalidations.isEmpty())
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    invalidations.iterator().next().causeDescription,
                    new IdentifiableResponse(response).toString()
            ));

        return response;
    }
}
