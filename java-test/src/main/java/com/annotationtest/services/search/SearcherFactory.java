package com.annotationtest.services.search;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SearcherFactory {
    private Iterator<AbstractSearcher> searcherIterator;

    public SearcherFactory() {
        ServiceLoader<AbstractSearcher> searchers = ServiceLoader.load(AbstractSearcher.class);
        this.searcherIterator = searchers.iterator();
    }

    public void search() {
        for (; searcherIterator.hasNext(); ) {
            searcherIterator.next().doSearch();
        }
    }

    public static void main(String[] args) {
        SearcherFactory searcherFactory = new SearcherFactory();
        searcherFactory.search();
    }
}
