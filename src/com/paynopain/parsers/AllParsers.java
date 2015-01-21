package com.paynopain.parsers;

import com.paynopain.commons.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllParsers<DataStructure, OutputObject> implements Function<DataStructure, List<OutputObject>> {
    private final Collection<? extends Function<DataStructure, OutputObject>> parsers;

    public AllParsers(Collection<? extends Function<DataStructure, OutputObject>> parsers) {
        this.parsers = parsers;
    }

    @Override
    public List<OutputObject> apply(DataStructure input) throws IllegalArgumentException {
        final ArrayList<OutputObject> outputObjects = new ArrayList<OutputObject>();

        for (Function<DataStructure, OutputObject> p : parsers){
            outputObjects.add(p.apply(input));
        }

        return outputObjects;
    }
}
