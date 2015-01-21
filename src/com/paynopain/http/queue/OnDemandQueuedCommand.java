package com.paynopain.http.queue;

import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.actions.RequestComposer;
import com.paynopain.http.validation.ResponseValidator;

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
