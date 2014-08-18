package com.touchiteasy.http.actions;

import com.touchiteasy.commons.Function;
import com.touchiteasy.commons.FunctionAdapter;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ResourceRequester;
import com.touchiteasy.http.Response;

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

    private class ParserFunction implements Function<Response, Output> {
        private final ResponseParser<Output> responseParser;

        public ParserFunction(ResponseParser<Output> responseParser){
            this.responseParser = responseParser;
        }

        @Override
        public Output apply(Response response) throws RuntimeException {
            return responseParser.parse(response);
        }
    }

    private final FunctionAdapter<Input, Output, Request, Response> adapter;

    public RequesterAction(final ResourceRequester requester, final RequestComposer<Input> inputConverter, final ResponseParser<Output> outputConverter) {
        adapter = new FunctionAdapter<Input, Output, Request, Response>(
                new ResourceRequesterFunction(requester),
                new ComposerFunction(inputConverter),
                new ParserFunction(outputConverter)
        );
    }

    @Override
    public Output apply(Input input) throws RuntimeException {
        return adapter.apply(input);
    }
}
