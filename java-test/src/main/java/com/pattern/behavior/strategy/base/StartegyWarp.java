package com.pattern.behavior.strategy.base;

public class StartegyWarp {

    private Class<? extends Strategy> startegyClass;

    private Strategy instance;

    private StartegyWarp(Strategy strategy) {
        this.instance = strategy;
        this.startegyClass = strategy.getClass();
    }

    public static StartegyWarp create(Strategy strategy) {
        return new StartegyWarp(strategy);
    }

    public Strategy getStrategy(boolean isSingletion) throws IllegalAccessException, InstantiationException {
        if (isSingletion) {
            return instance;
        }
        return startegyClass.newInstance();
    }
}
