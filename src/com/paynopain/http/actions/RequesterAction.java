package com.paynopain.http.actions;

import com.paynopain.commons.Function;
import com.paynopain.commons.FunctionAdapter;
import com.paynopain.http.Request;
import com.paynopain.http.ResourceRequester;
import com.paynopain.http.Response;

public class RequesterAction<Input, Output> implements Function<Input, Output>{
    private static class ResourceRequesterFunction implements Function<Request, Response> {
        private final ResourceRequester requester;

        public ResourceRequesterFunction(ResourceRequester requester){
            this.requester = requester;
        }

        @Override
        public Response apply(Request request) throws RuntimeException {
            return requester.run(request);
        }
    }

    private class ComposerFunction implements Function<Input, Request> {
        private final RequestComposer<Input> requestComposer;

        public ComposerFunction(RequestComposer<Input> requestComposer){
            this.requestComposer = requestComposer;
        }

        @Override
        public Request apply(Input input) throws RuntimeException {
            return requestComposer.compose(input);
        }
    }

    private class InterpreterFunction implements Function<Response, Output> {
        private final ResponseInterpreter<Output> responseInterpreter;

        public InterpreterFunction(ResponseInterpreter<Output> responseInterpreter){
            this.responseInterpreter = responseInterpreter;
        }

        @Override
        public Output apply(Response response) throws RuntimeException {
            return responseInterpreter.interpret(response);
        }
    }

    private final FunctionAdapter<Input, Output, Request, Response> adapter;

    public RequesterAction(final ResourceRequester requester, final RequestComposer<Input> inputConverter, final ResponseInterpreter<Output> outputConverter) {
        adapter = new FunctionAdapter<Input, Output, Request, Response>(
                new ResourceRequesterFunction(requester),
                new ComposerFunction(inputConverter),
                new InterpreterFunction(outputConverter)
        );
    }

    @Override
    public Output apply(Input input) throws RuntimeException {
        return adapter.apply(input);
    }
}
