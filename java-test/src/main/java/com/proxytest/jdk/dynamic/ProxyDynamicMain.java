package com.proxytest.jdk.dynamic;

import com.proxytest.ProxyTest;
import com.proxytest.impl.ProxyTestImpl;

import java.lang.reflect.Proxy;

public class ProxyDynamicMain {

    public static void main(String[] args) {
        ClassLoader classLoader = ProxyDynamicMain.class.getClassLoader();
        //调用代理对象的方法，最终调用InvocationHandler的invoke方法
        MyInvocationHandler handler = new MyInvocationHandler(ProxyTestImpl.class);
        //动态生成一个实现了ProxyTest的接口对象
        ProxyTest proxyTest = (ProxyTest) Proxy.newProxyInstance(classLoader,new Class[]{ProxyTest.class},handler);
        int resutl = proxyTest.add(1, 2);
        System.out.println(resutl);
    }
}
