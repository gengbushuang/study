package com.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<T> newLists(T... arrays) {
        List<T> tiems = new ArrayList<>(arrays.length);
        for (T t : arrays) {
            tiems.add(t);
        }
        return tiems;
    }
}
