package com.annotationtest.services.search;

public abstract class AbstractSearcher implements Searcher {
    @Override
    public void search() {
        this.doSearch();
    }

    public abstract void doSearch();
}
