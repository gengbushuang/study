package com.pattern.create.singleton;

public class SingletionSub {

    private SingletionSub() {
    }

    public static SingletionSub getInstance() {
        return SingletionHelp.singletionSub;
    }

    private static class SingletionHelp {
        private static SingletionSub singletionSub = new SingletionSub();
    }
}
