package com.reference;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 软引用
 */
public class SoftReferenceTest {

    public static void main(String[] args) {
        int count = 10;
        List<SoftReference<byte[]>> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            byte[] buff = new byte[1024 * 1024];
            //当内存要抛出OutOfMemoryError时候，进行了垃圾回收进行SoftReference的回收
            SoftReference<byte[]> sr = new SoftReference<>(buff);
            list.add(sr);
        }

        //当主动调用gc模式，内存还充足情况下，不会进行回收SoftReference的引用对象，最后打印对象不为null
        /**
         * [GC (System.gc())  1688K->1746K(3072K), 0.0002982 secs]
         * [Full GC (System.gc())  1746K->1682K(3072K), 0.0024731 secs]
         */
        System.gc();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {
            SoftReference<byte[]> softReference = list.get(i);
            System.out.println(softReference.get());
        }

    }
}
