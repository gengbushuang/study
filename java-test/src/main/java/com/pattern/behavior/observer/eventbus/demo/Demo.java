package com.pattern.behavior.observer.eventbus.demo;

import com.pattern.behavior.observer.eventbus.EventBus;

public class Demo {
    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        eventBus.register(new AObServer());
        eventBus.register(new BObserver());
        eventBus.register(new CObserver());


//        eventBus.post(new XMsg());
        eventBus.post(new YMsg());
//        eventBus.post(new ZMsg());
    }
}
