package com;

public class HelloWorldImpl implements HelloWord {
    @Override
    public String sayHello(String name) {
        return name + " hello";
    }

    @Override
    public A sss(P p) {
        A a = new A();
        a.setMsg(p.getA() + p.getB());
        return a;
    }
}
