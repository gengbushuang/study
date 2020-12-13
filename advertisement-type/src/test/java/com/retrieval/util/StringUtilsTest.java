package com.retrieval.util;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testFormat(){
        String s = StringUtils.format("nnnnn(%s) %s is  ssff %s ", "开始",2,3);
        System.out.println(s);
    }
}
