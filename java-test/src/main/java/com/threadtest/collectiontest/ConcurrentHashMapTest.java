package com.threadtest.collectiontest;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();


    public void testPut(){
        String key = "key";
        String value = "value";
        //先计算key的hash值
        //如果table为null，要先进行初始化,如果是第一次先对sizectl进行-1cas设置，
        // 如果在对table进行数组分配时候，第二个线程来访问table还是null，sizectl是-1就进行Thread.yield()等待
        //table的数组分配完成，根据hash值进行cas计算数组下标获取node，如果node为null证明是第一次赋值，对table数组进行cas赋值
        //如果cas赋值失败，继续循环分配
        //如果node不为null，证明有冲突，对node进行加锁，进行添加
        //判断node是链接结构还是二叉树结构
        //如果node是链表结构，按照链表添加，往尾部添加，并且对节点个数计数，
        //如果node是红黑树，按照红黑树添加，计数等于2
        //释放完锁后，判断计数如果大于等于8，转换红黑树
        //  转换红黑树过程
                //如果table的长度小于64
                    //
                //
        map.put(key,value);
    }

    public static void main(String[] args) {
        System.out.println((1<<30)>>> 1);
        int c = 49;
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n);
        new ConcurrentHashMapTest().testPut();
    }
}
