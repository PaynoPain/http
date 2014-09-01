package com.touchiteasy.http.actions;

import com.touchiteasy.http.Response;

public interface ResponseInterpreter<Output> {
    public Output interpret(Response response) throws IllegalArgumentException;
}
