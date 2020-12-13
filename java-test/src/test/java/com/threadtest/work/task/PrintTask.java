package com.threadtest.work.task;

public class PrintTask implements Task {
    @Override
    public void run(TaskContext context) {
        if (context instanceof NumberContext) {
            System.out.println(((NumberContext) context).getI());
        }
    }
}
