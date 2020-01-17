package org.java.thread.promise;

import java.util.function.Consumer;

public interface Executor<R1, R2 extends Throwable> {

    void executor(Consumer<R1> resolve, Consumer<R2> reject);
}
