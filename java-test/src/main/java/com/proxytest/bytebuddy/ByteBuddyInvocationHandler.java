package com.proxytest.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

public class ByteBuddyInvocationHandler {

    private Object target;

    public ByteBuddyInvocationHandler(Object _target){
        this.target = _target;
    }

    @RuntimeType
    public Object invoke(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Throwable{
        Object result = method.invoke(target, args);
        return result;
    }
}
