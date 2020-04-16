package com.utils;

import com.interfacetype.sort.SortInterface;

public class SortUtil {

    public static void sort(SortInterface sortInterface) {
        for (int i = 1; i < sortInterface.len(); i++) {
            if (sortInterface.less(i - 1, i)) {
                sortInterface.swap(i - 1, i);
            }
        }
    }
}
