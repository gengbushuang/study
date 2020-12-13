package com.threadtest.work.task;

public class AddTask implements Task {

    private final int i;

    public AddTask(int i) {
        this.i = i;
    }

    @Override
    public void run(TaskContext context) {
        if (context instanceof NumberContext) {
            NumberContext numberContext = (NumberContext) context;
            numberContext.setI(numberContext.getI() + i);
        }
    }
}
