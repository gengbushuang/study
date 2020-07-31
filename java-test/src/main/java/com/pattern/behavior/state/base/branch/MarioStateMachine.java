package com.pattern.behavior.state.base.branch;

/**
 * if-else来表示状态之间转换
 */
public class MarioStateMachine {
    private int score;
    private State currentState;

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }

    //获得蘑菇，超级马里奥
    public void obtainMushRoom() {
        if (currentState == State.SMALL) {
            score += 100;
            currentState = State.SUPER;
        }
    }

    //获得斗篷，斗篷马里奥
    public void obtainCape() {
        if (currentState == State.SMALL ||
                currentState == State.SUPER) {
            score += 200;
            currentState = State.CAPE;
        }
    }

    //获得火焰，火焰马里奥
    public void obtainFireFlower() {
        if (currentState == State.SMALL ||
                currentState == State.SUPER) {
            score += 300;
            currentState = State.FIRE;
        }
    }

    //遇到怪物
    public void meetMonster() {
        if (currentState == State.SUPER) {
            score -= 100;
            currentState = State.SMALL;
        } else if (currentState == State.CAPE) {
            score -= 200;
            currentState = State.SMALL;
        } else if (currentState == State.FIRE) {
            score -= 300;
            currentState = State.SMALL;
        }
    }

    public int getScore() {
        return score;
    }

    public State getCurrentState() {
        return currentState;
    }
}
