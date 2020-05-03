package com.annotationtest.services.search.impl;

import com.annotationtest.services.search.AbstractSearcher;
import com.google.auto.service.AutoService;
//这个主要自动生成 java spi META-INF/services
@AutoService(AbstractSearcher.class)
public class IndexSearcher extends AbstractSearcher {
    @Override
    public void doSearch() {
        System.out.println("IndexSearcher");
    }
}
