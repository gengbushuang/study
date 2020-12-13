package com.threadtest.work.task;

public class TwoTask implements Task {
    @Override
    public void run(TaskContext o) {
        System.out.println("two run--");
    }
}
