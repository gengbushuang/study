package com.retrieval.util;

import com.retrieval.indexer.AbsIndexer;
import com.retrieval.indexer.IndexType;
import com.retrieval.indexer.basic.IndexerBasic;
import com.retrieval.indexer.section.IndexerSection;

public class IndexerFactory {

    public static AbsIndexer createIndexer(String name) {
        IndexType indexType = AssignmentDict.getIndexType(name);
        if (IndexType.BASIC == indexType) {
            return new IndexerBasic(name);
        } else if (IndexType.SECTION == indexType) {
            return new IndexerSection(name);
        }
        return null;
    }
}
