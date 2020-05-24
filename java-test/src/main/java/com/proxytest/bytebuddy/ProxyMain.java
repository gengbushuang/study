package com.proxytest.bytebuddy;

import com.proxytest.ProxyTest;
import com.proxytest.impl.ProxyTestImpl;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class ProxyMain {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        ByteBuddyInvocationHandler invocationHandler = new ByteBuddyInvocationHandler(new ProxyTestImpl());
        Class<? extends ProxyTest> aClass = new ByteBuddy()
                //// 指定父类
                .subclass(ProxyTest.class)
                // 根据名称来匹配需要拦截的方法
                .method(ElementMatchers.isDeclaredBy(ProxyTest.class))
                // 拦截方法调用，返回固定值
                .intercept(MethodDelegation.to(invocationHandler, "handler"))
                // 产生字节码
                .make()
                // 加载类
                .load(ProxyTest.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                // 获得Class对象
                .getLoaded();

        ProxyTest proxyTest = aClass.newInstance();
        int resutl = proxyTest.add(3, 2);
        System.out.println(resutl);
    }
}
