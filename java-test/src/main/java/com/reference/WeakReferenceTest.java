package com.reference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 弱引用
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        int count = 10;
        List<WeakReference<byte[]>> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            byte[] buff = new byte[1024 * 1024];
            WeakReference<byte[]> wr = new WeakReference<>(buff);
            list.add(wr);
        }
        //当主动调用gc模式，不管内存是不是不足，都会进行回收WeakReference的引用对象，最后打印对象为null
        /**
         * [GC (System.gc())  1735K->1767K(3584K), 0.0002597 secs]
         * [Full GC (System.gc())  1767K->711K(3584K), 0.0023511 secs]
         */
        System.gc();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {
            WeakReference<byte[]> weakReference = list.get(i);
            System.out.println(weakReference.get());
        }
    }
}
