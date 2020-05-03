package com.annotationtest.services.search.impl;

import com.annotationtest.services.search.AbstractSearcher;
import com.google.auto.service.AutoService;

@AutoService(AbstractSearcher.class)
public class RankSearcher extends AbstractSearcher {
    @Override
    public void doSearch() {
        System.out.println("RankSearcher");
    }
}
