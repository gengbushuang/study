package com.ad.mixer.reranking.strategy;

import com.ad.mixer.reranking.Reranking;
import com.ad.mixer.reranking.RerankingBuilder;

public class FilterRerankingBuilder extends RerankingBuilder {
    @Override
    public String name() {
        return "filter_reranking";
    }

    @Override
    public Reranking createReranking() {
        return new FilterReranking();
    }
}
