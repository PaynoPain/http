package com.paynopain.parsers;

import com.paynopain.commons.Function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllSuitableParsers <DataStructure, OutputObject> implements Function<DataStructure, List<OutputObject>> {
    private final Collection<? extends Function<DataStructure, OutputObject>> parsers;

    public AllSuitableParsers(Collection<? extends Function<DataStructure, OutputObject>> parsers) {
        if (parsers.size() == 0)
            throw new IllegalArgumentException("Can't work without any parsers to ask!");

        this.parsers = parsers;
    }

    @Override
    public List<OutputObject> apply(DataStructure input) throws IllegalArgumentException {
        final ArrayList<OutputObject> parsedValues = new ArrayList<OutputObject>();
        final ArrayList<IllegalArgumentException> exceptions = new ArrayList<IllegalArgumentException>();

        for (Function<DataStructure, OutputObject> parser : parsers){
            try {
                parsedValues.add(parser.apply(input));
            } catch (IllegalArgumentException parseException){
                exceptions.add(parseException);
            }
        }

        if (parsedValues.size() == 0){
            throw constructException(exceptions, input);
        }

        return parsedValues;
    }

    private IllegalArgumentException constructException(ArrayList<IllegalArgumentException> exceptions, DataStructure input) {
        final StringWriter stackTraces = new StringWriter();

        for (IllegalArgumentException e : exceptions){
            e.printStackTrace(new PrintWriter(stackTraces));
        }

        return new IllegalArgumentException(String.format(
                "There was no suitable parser for the input:\n%s\n%s",
                input.toString(),
                stackTraces.toString()
        ));
    }
}
