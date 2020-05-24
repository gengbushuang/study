package com.proxytest.impl;

import com.proxytest.ProxyTest;

public class ProxyTestImpl implements ProxyTest {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
