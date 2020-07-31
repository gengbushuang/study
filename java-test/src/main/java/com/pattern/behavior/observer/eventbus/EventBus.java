package com.pattern.behavior.observer.eventbus;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {

    private Executor executor;

    private ObserverRegistry observerRegistry = new ObserverRegistry();

    public EventBus() {
        this(Executors.newSingleThreadExecutor());
    }

    public EventBus(Executor executor) {
        this.executor = executor;
    }

    public void register(Object observer) {
        observerRegistry.register(observer);
    }

    public void post(Object event) {
        List<ObserverAction> subscribers = observerRegistry.getSubscribers(event);
        for (ObserverAction action : subscribers) {
            executor.execute(() -> {
                action.execute(event);
            });
        }
    }

}
