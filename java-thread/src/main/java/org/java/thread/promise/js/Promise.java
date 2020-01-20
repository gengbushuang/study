package org.java.thread.promise.js;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Promise<V, T extends Throwable> {

    <O> Promise<O, T> andThen(Function<V, O> onFulfilled, Consumer<T> onRejected);

    <O> Promise<O, T> andThen(Function<V, O> onFulfilled);

    Promise<V, T> andThen(Consumer<V> onFulfilled, Consumer<T> onRejected);

    Promise<V, T> andThen(Consumer<V> onFulfilled);
}
