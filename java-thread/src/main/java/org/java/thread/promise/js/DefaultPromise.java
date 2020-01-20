package org.java.thread.promise.js;


import org.java.thread.promise.js.function.Executor;
import org.java.thread.promise.js.function.Reject;
import org.java.thread.promise.js.function.Resolve;

import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultPromise<V, T extends Throwable> implements Promise<V, T> {

    //一个 promise 有且只有一个状态（pending，fulfilled，rejected 其中之一）
    private Status state = Status.STATE_PENDING;

    private V value;

    private T reason;

    public DefaultPromise(Executor executor) {

        Resolve<V> resolve = r1 -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_FULFILLED;
                this.value = r1;
            }
        };

        Reject<T> reject = r2 -> {
            if (this.state == Status.STATE_PENDING) {
                this.state = Status.STATE_REJECTED;
                this.reason = r2;
            }
        };
        try {
            executor._(resolve, reject);
        } catch (Throwable t) {
            reject._((T) t);
        }
    }

    @Override
    public <O> Promise<O, T> andThen(Function<V, O> onFulfilled, Consumer<T> onRejected) {
        Executor<O, T> executor = (resolve, reject) -> {
            if (this.state == Status.STATE_FULFILLED) {
                O result = onFulfilled.apply(value);
                resolve._(result);
            }
            if (this.state == Status.STATE_REJECTED) {
                onRejected.accept(reason);
                reject._(reason);
            }
        };
        return new DefaultPromise(executor);
    }


    @Override
    public <O> Promise<O, T> andThen(Function<V, O> onFulfilled) {
        Executor<O, T> executor = (resolve, reject) -> {
            if (this.state == Status.STATE_FULFILLED) {
                O result = onFulfilled.apply(value);
                resolve._(result);
            }
            if (this.state == Status.STATE_REJECTED) {
                reject._(reason);
            }
        };
        return new DefaultPromise(executor);
    }

    @Override
    public Promise<V, T> andThen(Consumer<V> onFulfilled, Consumer<T> onRejected) {
        Executor<V, T> executor = (resolve, reject) -> {
            if (this.state == Status.STATE_FULFILLED) {
                onFulfilled.accept(value);
                resolve._(value);
            }
            if (this.state == Status.STATE_REJECTED) {
                onRejected.accept(reason);
                reject._(reason);
            }
        };
        return new DefaultPromise(executor);
    }

    @Override
    public Promise<V, T> andThen(Consumer<V> onFulfilled) {
        Executor<V, T> executor = (resolve, reject) -> {
            if (this.state == Status.STATE_FULFILLED) {
                onFulfilled.accept(value);
                resolve._(value);
            }
            if (this.state == Status.STATE_REJECTED) {
                reject._(reason);
            }
        };
        return new DefaultPromise(executor);
    }
}
