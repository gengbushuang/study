package com.pattern.behavior.state.base.state;

public class SuperMario implements IMario {

    private MarioStateMachine stateMachine;

    public SuperMario(MarioStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getState() {
        return State.SUPER;
    }

    @Override
    public void obtainMushRoom() {

    }

    @Override
    public void obtainCape() {
        stateMachine.setCurentState(new CapeMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 200);
    }

    @Override
    public void obtainFireFlower() {
        stateMachine.setCurentState(new FireMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 300);
    }

    @Override
    public void meetMonster() {
        stateMachine.setCurentState(new SmallMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() - 100);
    }
}
