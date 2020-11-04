package com.retrieval.model;

import java.util.ArrayList;
import java.util.List;

public class Dnf {
    private int docid;

    private String mode;

    private List<Disjunction> disjunctions = new ArrayList<>();


    public List<Disjunction> getDisjunctions() {
        return disjunctions;
    }

    public int getDocid() {
        return docid;
    }
}
