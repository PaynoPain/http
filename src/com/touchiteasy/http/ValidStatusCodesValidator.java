package com.touchiteasy.http;

import java.util.List;

public class ValidStatusCodesValidator implements ResponseValidatingRequester.ResponseValidator {
    public ValidStatusCodesValidator(List<Integer> validStatusCodes){}

    @Override
    public boolean isValid(Response response) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getCauseDescription() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
