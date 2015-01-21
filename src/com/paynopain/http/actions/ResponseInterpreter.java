package com.paynopain.http.actions;

import com.paynopain.http.Response;

public interface ResponseInterpreter<Output> {
    public Output interpret(Response response) throws IllegalArgumentException;
}
