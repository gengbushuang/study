package com.threadtest.promise.js;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

public class PromiseTest {

    private final Logger log = LogManager.getLogger(this.getClass());

    public void test1() {
        Promise my = new Promise((resolve, reject) -> {
            log.info("执行");
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("resolve执行");
                resolve.resolve(3);
            }).start();
        });

        my.then(resolve -> {
                    log.info(resolve);
                }
                , reject -> {
                    log.error(reject);
                });
    }

    public void test2() {
        Promise my = new Promise((resolve, reject) -> {
            log.info("执行");
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("resolve执行");
                resolve.resolve(4);
            }).start();
        });

        my.thenResolve((r1) -> {
            log.info("res1:{}", r1);
        });

        my.thenResolve((r2) -> {
            log.info("res2:{}", r2);
        });
        ;
    }

    public void test3() {
        Promise2 my = new Promise2((resolve, reject) -> {
            log.info("执行");
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("resolve执行");
                resolve.resolve(4);
            }).start();
        });

        my.thenResolve((r1) -> {
            log.info("first:{}", r1);
            return (int) r1 + 3;
        }).thenResolve(r2 -> {
            log.info("second:{}", r2);
            return (int) r2 + 3;
        });

//        my.then2(null, null)
//                .then2(r1 -> {
//                }, r2 -> {
//                })
//        ;
    }

    public void test() {
//        Stream.of(1,2,3,4).map(x->x.toString()).map()
    }

    public static void main(String[] args) {
        new PromiseTest().test3();
    }
}
