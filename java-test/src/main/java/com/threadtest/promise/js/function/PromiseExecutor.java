package com.threadtest.promise.js.function;

@FunctionalInterface
public interface PromiseExecutor {

    public void executor(PromiseResolve resolve,PromiseReject reject);
}
