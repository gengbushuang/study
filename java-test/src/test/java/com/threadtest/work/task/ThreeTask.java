package com.threadtest.work.task;

public class ThreeTask implements Task {

    @Override
    public void run(TaskContext o) {
        System.out.println("three run--");
    }

}
