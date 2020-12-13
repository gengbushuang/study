package com.ad.mixer.reranking;

public abstract class RerankingBuilder {

    public abstract String name();

    public abstract Reranking createReranking();
}
