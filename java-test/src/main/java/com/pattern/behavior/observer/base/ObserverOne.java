package com.pattern.behavior.observer.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObserverOne implements Observer {
    private final Logger log = LogManager.getLogger(ObserverOne.class);
    @Override
    public void update(String msg) {
        log.info("ObserverOne is notify");
    }
}
