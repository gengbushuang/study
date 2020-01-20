package org.java.thread.promise.js.function;

public interface Executor<V, T extends Throwable> {

    public void _(Resolve<V> resolve, Reject<T> reject);
}
