package com.threadtest.work.task;

public class FixeTask implements Task {
    @Override
    public void run(TaskContext o) {
        System.out.println("fixe run--");
    }
}
