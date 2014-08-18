package com.touchiteasy.commons;

public class FunctionAdapter<Input, Output, InternalInput, InternalOutput> implements Function<Input, Output> {
    private final Function<InternalInput, InternalOutput> internalFunction;
    private final Function<Input, InternalInput> inputAdapter;
    private final Function<InternalOutput, Output> outputAdapter;

    public FunctionAdapter(
            Function<InternalInput, InternalOutput> internalFunction,
            Function<Input, InternalInput> inputAdapter, Function<InternalOutput, Output> outputAdapter
    ){
        this.internalFunction = internalFunction;
        this.inputAdapter = inputAdapter;
        this.outputAdapter = outputAdapter;
    }

    @Override
    public Output apply(Input input) {
        final InternalInput internalInput = inputAdapter.apply(input);
        final InternalOutput internalOutput = internalFunction.apply(internalInput);
        final Output output = outputAdapter.apply(internalOutput);
        return output;
    }
}
