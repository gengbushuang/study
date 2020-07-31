package com.pattern.behavior.observer.eventbus.demo;

import com.pattern.behavior.observer.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BObserver {

    private final Logger log = LogManager.getLogger(BObserver.class);

    @Subscribe
    public void f1(YMsg event){
        log.info("BObserver f1 is YMsg");
    }

    @Subscribe
    public void f2(ZMsg event){
        log.info("BObserver f2 is ZMsg");
    }
}
