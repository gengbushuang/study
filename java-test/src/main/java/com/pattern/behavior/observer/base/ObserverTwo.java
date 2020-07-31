package com.pattern.behavior.observer.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObserverTwo implements Observer {
    private final Logger log = LogManager.getLogger(ObserverTwo.class);

    @Override
    public void update(String msg) {
        log.info("ObserverTwo is notify");
    }
}
