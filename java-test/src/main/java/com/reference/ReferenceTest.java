package com.reference;

import java.util.ArrayList;
import java.util.List;

public class ReferenceTest {
    public static void main(String[] args) {
        int count = 10;
        List<byte[]> list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            byte[] buff = new byte[1024 * 1024];
            list.add(buff);
        }

        for (int i = 0; i < count; i++) {
            byte[] bytes = list.get(i);
            System.out.println(bytes);
        }
    }
}
