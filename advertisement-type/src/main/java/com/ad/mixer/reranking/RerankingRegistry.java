package com.ad.mixer.reranking;

import java.util.HashMap;
import java.util.Map;

public class RerankingRegistry {

    private Map<String, RerankingBuilder> registryReranking = new HashMap<>();

    public void register(RerankingBuilder builder) {
        registryReranking.put(builder.name(), builder);
    }


    public Reranking createReranking(String name) {
        RerankingBuilder builder = registryReranking.get(name);
        if (builder != null) {
            return builder.createReranking();
        }
        return null;
    }
}
