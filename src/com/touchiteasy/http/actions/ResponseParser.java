package com.touchiteasy.http.actions;

import com.touchiteasy.http.Response;

public interface ResponseParser<Output> {
    public Output parse(Response response) throws IllegalArgumentException;
}
