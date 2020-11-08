package com.retrieval;

import com.proto.indexer.Indexer;

public interface IndexProcessor {

    public void add(Indexer.TargetingDNF dnf, int localId);

    public void update(Indexer.TargetingDNF dnf, int localId);

    public void delete(Indexer.TargetingDNF dnf, int localId);

}
