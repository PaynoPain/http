package com.paynopain.http.actions;

import com.paynopain.http.Request;

public interface RequestComposer<Input> {
    public Request compose(Input input);
}
