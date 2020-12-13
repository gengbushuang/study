package com.threadtest.work.task;

public class NumberContext extends TaskContext {

    private int i;

    public NumberContext(int i){
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
