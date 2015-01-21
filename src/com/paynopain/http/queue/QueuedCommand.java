package com.paynopain.http.queue;

import com.paynopain.commons.Function;
import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.actions.RequestComposer;
import com.paynopain.http.validation.ResponseValidatingRequester;
import com.paynopain.http.validation.ResponseValidator;

public class QueuedCommand<Input> implements Function<Input, Void>, Flushable {
    private final QueueFlusher flusher;
    private final QueueStorage<Request> queue;
    private final RequestComposer<Input> composer;

    public QueuedCommand(final ResourceRequester base, final QueueStorage<Request> queue,
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