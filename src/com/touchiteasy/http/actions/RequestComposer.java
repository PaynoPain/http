package com.touchiteasy.http.actions;

import com.touchiteasy.http.Request;

public interface RequestComposer<Input> {
    public Request compose(Input input);
}
