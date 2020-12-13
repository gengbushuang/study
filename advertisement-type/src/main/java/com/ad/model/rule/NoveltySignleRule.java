package com.ad.model.rule;

import java.util.Collections;
import java.util.List;

public class NoveltySignleRule {
    private long posId;

    private List<Rule> posRules;


    public List<Rule> getRules() {
        return Collections.unmodifiableList(this.posRules);
    }
}
