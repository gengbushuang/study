package com.ad.mixer.reranking.strategy;

import com.ad.mixer.reranking.Reranking;
import com.ad.mixer.reranking.RerankingBuilder;

public class SimpleRerankingBuilder extends RerankingBuilder {
    @Override
    public String name() {
        return "simple_reranking";
    }

    @Override
    public Reranking createReranking() {
        return new SimpleReranking();
    }
}
