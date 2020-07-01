package com.proxytest.impl;

import com.proxytest.ProxyTest;
import com.proxytest.ProxyTest2;

public class ProxyTestImpl implements ProxyTest, ProxyTest2 {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int add2(int a, int b) {
        return a + b + 2;
    }
}
