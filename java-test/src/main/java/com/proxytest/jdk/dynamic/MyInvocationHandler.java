package com.proxytest.jdk.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
    //目标对象
    private final Class<?> protocol;

    public MyInvocationHandler(Class<?> _protocol){
        this.protocol = _protocol;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //实例化目标对象
        Object instance = protocol.newInstance();
        //调用目标对象对应的方法
        Object resutl = method.invoke(instance, args);
        return resutl;
    }
}
