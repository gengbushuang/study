package com.threadtest.collectiontest.stack;

import com.threadtest.collectiontest.stack.LockFreeStack;

public class lockFreeStackTest {
    public static void main(String[] args) {
        final LockFreeStack<Integer> stack = new LockFreeStack<>();

        final Thread pushThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    stack.push(i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        final Thread popThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    final Integer elem = stack.pop();
                    System.out.println("popped " + elem);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pushThread.start();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        popThread.start();
    }
}
