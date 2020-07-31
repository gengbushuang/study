package com.pattern.behavior.state.base.branch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Demo {

    private static final Logger log = LogManager.getLogger(Demo.class);

    public static void main(String[] args) {

        MarioStateMachine mario = new MarioStateMachine();
        mario.obtainMushRoom();
        mario.obtainCape();
        mario.meetMonster();
        mario.obtainMushRoom();
        int score = mario.getScore();
        State currentState = mario.getCurrentState();
        log.info("mario score {}; state {}",score,currentState);
    }
}
