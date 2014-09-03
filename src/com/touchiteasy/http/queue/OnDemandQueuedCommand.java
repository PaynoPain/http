package com.touchiteasy.http.queue;

import com.touchiteasy.commons.Function;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.actions.RequestComposer;
import com.touchiteasy.http.validation.ResponseValidatingRequester;
import com.touchiteasy.http.validation.ResponseValidator;

public class OnDemandQueuedCommand<Input> implements Function<Input, Void>, Flushable {
    private final QueueFlusher flusher;
    private final QueueStorage<Request> queue;
    private final RequestComposer<Input> composer;

    public OnDemandQueuedCommand(final ResourceRequester base, final QueueStorage<Request> queue,
                                 final RequestComposer<Input> composer, final ResponseValidator validator){
        this.queue = queue;
        this.composer = composer;
        this.flusher = new QueueFlusher(
                new ResponseValidatingRequester(
                        base,
                        validator
                ),
                queue
        );
    }

    @Override
    public Void apply(Input input) throws RuntimeException {
        queue.add(composer.compose(input));
        flusher.flush();
        return null;
    }

    @Override
    public boolean canFlush() {
        return flusher.canFlush();
    }

    @Override
    public void flush() {
        flusher.flush();
    }
}