package com.pattern.behavior.state.base.state;

public class CapeMario implements IMario {

    private MarioStateMachine stateMachine;

    public CapeMario(MarioStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getState() {
        return State.CAPE;
    }

    @Override
    public void obtainMushRoom() {

    }

    @Override
    public void obtainCape() {

    }

    @Override
    public void obtainFireFlower() {

    }

    @Override
    public void meetMonster() {
        stateMachine.setCurentState(new SmallMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() - 200);
    }
}
