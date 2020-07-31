package com.pattern.behavior.observer.eventbus.demo;

import com.pattern.behavior.observer.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CObserver {

    private final Logger log = LogManager.getLogger(CObserver.class);

    @Subscribe
    public void f(ZMsg event){
        log.info("CObserver is ZMsg");
    }
}
