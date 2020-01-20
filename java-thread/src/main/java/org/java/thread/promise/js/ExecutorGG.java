package org.java.thread.promise.js;

import java.util.function.Consumer;

public interface ExecutorGG<R1, R2 extends Throwable> {

    void executor(Consumer<R1> resolve, Consumer<R2> reject);
}
