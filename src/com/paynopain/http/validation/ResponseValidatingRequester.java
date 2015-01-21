package com.paynopain.http.validation;

import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.Response;

public class ResponseValidatingRequester implements ResourceRequester {
    private final ResourceRequester baseRequester;
    private final ResponseChecker checker;

    public ResponseValidatingRequester(ResourceRequester baseRequester, ResponseValidator validator) {
        this.baseRequester = baseRequester;
        this.checker = new ResponseChecker(validator);
    }

    @Override
    public Response run(Request request) {
        Response response = baseRequester.run(request);
        checker.check(response);
        return response;
    }
}
