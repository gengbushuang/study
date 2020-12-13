package com.jvm.memory;

/**
 *
 *
 * jvm内存分布
 *
 * 方法区--》线程--》程序计算器--》方法虚拟栈
 *  |                                   |
 *  |                                   |
 * 线程                                 |
 *  |                                   |
 *  |                                   |
 * 程序计算器                           |
 *  |                                   |
 *  |                                   |
 * 方法虚拟栈-----》         堆内存
 *
 *
 *
 */
public class JavaMemory {

    public static void main(String[] args) {
        //jvm启动后，加载JavaMemory.class,JavaMethod.class，放到方法区里面
        //加载完成后，要执行.class里面代码，需要有一个能记录代码执行到哪里的标记，这个就是程序计数器
        //java是个多线程并发执行，程序计算器是线程私有的
        //线程执行方法中的代码时候，方法里面会有一些局部变量需要保存，这里就是虚拟方法栈
        //还有当遇到new一个对象的时候，会给这个对象分配内存到堆里面，而虚拟方法栈会有一个地址指向堆里面的对象


        JavaMethod method = new JavaMethod();
        int a = 1;
        int b = 2;
        int add = method.add(a, b);
    }
}
