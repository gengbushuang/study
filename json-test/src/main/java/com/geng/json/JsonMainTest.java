package com.geng.json;

import java.io.IOException;

public class JsonMainTest {

    public static void main(String[] args) {
//        String sss = "{\"orderId\":\"1234\"}";
//        OrdersJsonReader ordersJsonReader = new OrdersJsonReader(ObjectJsonFactory.create());
//
//        try {
//            Orders orders = ordersJsonReader.readBidRequest(sss);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        int a = 0xEF;
        int b = 0xABCDEF;
        int c = 0;
        int d = 0xFFFFFF;
//        System.out.println((~c)&(a&b));
        System.out.println(d|a);
//        System.out.println(a && b);

    }
}
