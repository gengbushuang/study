package org.java.thread.promise;

import java.util.function.Consumer;

public class Promise<R1, R2 extends Throwable> {
    //一个 promise 有且只有一个状态（pending，fulfilled，rejected 其中之一）
    private Status state = Status.STATE_PENDING;

    private R1 value;

    private R2 reason;

    //2.1.1 pending 状态时：
    //2.1.1.1 可能会转变为 fulfilled 或 rejected 状态
    public Promise(Executor<R1,R2> executor) {

        //2.1.2 fulfilled 状态时：
        //2.1.2.1 不能再状态为任何其他状态
        //2.1.2.2 必须有一个 value，且不可改变
        Consumer<R1> resolve = r1 -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_REJECTED;
                this.value = r1;
            }
        };

        //2.1.3 rejected 状态时：
        //2.1.3.1 不能再状态为任何其他状态
        //2.1.3.2 必须有一个 reason，且不可改变
        Consumer<R2> reject = r2 -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_FULFILLED;
                this.reason = r2;
            }
        };
        try {
            executor.executor(resolve, reject);
        } catch (Exception e) {
            reject.accept((R2) e);
        }
    }

    public <R1, R2 extends Throwable> void then(Consumer<R1> onFulfilled,Consumer<R2> onRejected){
        if(this.state==Status.STATE_REJECTED){
            onFulfilled.accept((R1) value);
        }

        if(this.state==Status.STATE_FULFILLED){
            onRejected.accept((R2) reason);
        }
    }
}
