package com.threadtest.work.task;

public class OneTask implements Task {
    @Override
    public void run(TaskContext o) {
        System.out.println("one run--");
    }

}
