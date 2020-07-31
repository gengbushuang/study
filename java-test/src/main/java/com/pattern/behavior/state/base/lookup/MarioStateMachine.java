package com.pattern.behavior.state.base.lookup;

/**
 * 二维数组来表示状态之间转换
 *             TO_SUPER(0)     TO_FIRE(1)      TO_CAPE(3)       TO_SMALL(4)
 * SMALL(0)    super/+100      fire/+300       cape/+200
 * SUPER(1)                    fire/+300       cape/+200        small/-100
 * FIRE (2)                                                     small/-300
 * CAPE (3)                                                     small/-200
 *
 */
public class MarioStateMachine {
    private int score;
    private State currentState;

    private static final State[][] transitionTable = {
            {State.SUPER,State.FIRE,State.CAPE,State.SMALL},
            {State.SUPER,State.FIRE,State.CAPE,State.SMALL},
            {State.FIRE,State.FIRE,State.FIRE,State.SMALL},
            {State.CAPE,State.CAPE,State.CAPE,State.SMALL}
            };

    private static final int[][] scoreTable = {
            {+100, +300, +200, +0},
            {+0, +300, +200, -100},
            {+0, +0, +0, -300},
            {+0, +0, +0, -200}
    };

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }

    //获得蘑菇，超级马里奥
    public void obtainMushRoom() {
        send(Event.TO_SUPER);
    }



    //获得斗篷，斗篷马里奥
    public void obtainCape() {
        send(Event.TO_CAPE);
    }

    //获得火焰，火焰马里奥
    public void obtainFireFlower() {
        send(Event.TO_FIRE);
    }

    //遇到怪物
    public void meetMonster() {
        send(Event.TO_SMALL);
    }

    private void send(Event event) {
        int nextState = event.getValue();
        int curState = currentState.getValue();
        this.currentState = transitionTable[curState][nextState];
        this.score+=scoreTable[curState][nextState];
    }

    public int getScore() {
        return score;
    }

    public State getCurrentState() {
        return currentState;
    }
}
