package com.pattern.behavior.observer.eventbus.demo;

import com.pattern.behavior.observer.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AObServer {

    private final Logger log = LogManager.getLogger(AObServer.class);

    @Subscribe
    public void f(XMsg event){
        log.info("AObServer is XMsg");
    }
}
