package com.touchiteasy.http.validation;

import com.touchiteasy.http.IdentifiableResponse;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

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

        final ResponseValidator.ValidationResult result = validator.analyse(response);
        if (!result.isValid)
            throw new InvalidResponseException(String.format(
                    "The following response is invalid because %s.\n%s",
                    result.causeDescription,
                    new IdentifiableResponse(response).toString()
            ));

        return response;
    }
}
