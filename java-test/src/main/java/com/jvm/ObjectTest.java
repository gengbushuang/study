package com.jvm;
//头对象---->mark(64位8字节)+klass指针(64位8字节)
public class ObjectTest {
    //4字节
    private int a;
//    //1字节
    private boolean b;
//    //8字节
    private double c;

    public static void main(String[] args) {

        System.out.println(ObjectSizeCalculator.getObjectSize(new ObjectTest()));
    }
}
