package com.pattern.behavior.state.base.state;

public interface IMario {

    State getState();

    //获得蘑菇，超级马里奥
    public void obtainMushRoom();

    //获得斗篷，斗篷马里奥
    public void obtainCape();

    //获得火焰，火焰马里奥
    public void obtainFireFlower();

    //遇到怪物
    public void meetMonster();
}
