package com.pattern.behavior.state.base.state;

/**
 * if-else来表示状态之间转换
 */
public class MarioStateMachine {
    private int score;
    private IMario curentState;

    public MarioStateMachine() {
        this.score = 0;
        this.curentState = new SmallMario(this);
    }

    //获得蘑菇，超级马里奥
    public void obtainMushRoom() {
        curentState.obtainMushRoom();
    }

    //获得斗篷，斗篷马里奥
    public void obtainCape() {
        curentState.obtainCape();
    }

    //获得火焰，火焰马里奥
    public void obtainFireFlower() {
        curentState.obtainFireFlower();
    }

    //遇到怪物
    public void meetMonster() {
        curentState.meetMonster();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public State getCurentState() {
        return curentState.getState();
    }

    public void setCurentState(IMario curentState) {
        this.curentState = curentState;
    }
}
