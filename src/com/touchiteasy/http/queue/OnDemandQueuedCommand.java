package com.touchiteasy.http.queue;

import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.actions.RequestComposer;
import com.touchiteasy.http.validation.ResponseValidator;

public class OnDemandQueuedCommand<Input> extends QueuedCommand<Input> {
    public OnDemandQueuedCommand(ResourceRequester base, QueueStorage<Request> queue,
                                 RequestComposer<Input> composer, ResponseValidator validator) {
        super(base, queue, composer, validator);
    }

    @Override
    public Void apply(Input input) throws RuntimeException {
        super.apply(input);
        this.flush();

        return null;
    }
}
