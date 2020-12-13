package com.threadtest.promise.js;

import com.threadtest.promise.js.function.PromiseExecutor;
import com.threadtest.promise.js.function.PromiseReject;
import com.threadtest.promise.js.function.PromiseResolve;

import java.util.ArrayDeque;

/**
 * https://promisesaplus.com/
 */
public class Promise<E extends Throwable> {
    //一个 promise 有且只有一个状态（pending，fulfilled，rejected 其中之一）
    private Status state = Status.STATE_PENDING;

    private Object value;

    private E reason;

    private ArrayDeque<PromiseResolve> onFulfilled = new ArrayDeque<>();
    private ArrayDeque<PromiseReject> onRejected = new ArrayDeque<>();

    //2.1.1 pending 状态时：
    //2.1.1.1 可能会转变为 fulfilled 或 rejected 状态
    public Promise(PromiseExecutor promiseExecutor) {

        //2.1.2 fulfilled 状态时：
        //2.1.2.1 不能再状态为任何其他状态
        //2.1.2.2 必须有一个 value，且不可改变
        PromiseResolve promiseResolve = v -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_REJECTED;
                this.value = v;
                onFulfilled.forEach(fn -> fn.resolve(this.value));
            }
        };

        //2.1.3 rejected 状态时：
        //2.1.3.1 不能再状态为任何其他状态
        //2.1.3.2 必须有一个 reason，且不可改变
        PromiseReject<E> promiseReject = r -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_FULFILLED;
                this.reason = r;
                onRejected.forEach(fn -> fn.reject(this.reason));
            }
        };

        try {
            promiseExecutor.executor(promiseResolve, promiseReject);
        } catch (Throwable e) {
            promiseReject.reject((E) e);
        }
    }

    public void thenResolve(PromiseResolve promiseResolve) {
        this.then(promiseResolve, null);
    }

    public void thenReject(PromiseReject promiseReject) {
        this.then(null, promiseReject);
    }

    public void then(PromiseResolve promiseResolve, PromiseReject promiseReject) {
        if (this.state == Status.STATE_REJECTED && promiseResolve instanceof PromiseResolve) {
            promiseResolve.resolve(this.value);
        }

        if (this.state == Status.STATE_FULFILLED && promiseReject instanceof PromiseReject) {
            promiseReject.reject(this.reason);
        }

        if (this.state == Status.STATE_PENDING) {
            if (promiseResolve instanceof PromiseResolve) {
                onFulfilled.add(promiseResolve);

            } else if (promiseReject instanceof PromiseReject) {
                onRejected.add(promiseReject);
            }
        }
    }

}
