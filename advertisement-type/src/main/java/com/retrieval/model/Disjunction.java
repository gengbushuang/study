package com.retrieval.model;

import java.util.ArrayList;
import java.util.List;

public class Disjunction {
    private List<Conjunction> conjunctions = new ArrayList<>();

    public List<Conjunction> getConjunctions() {
        return conjunctions;
    }
}
