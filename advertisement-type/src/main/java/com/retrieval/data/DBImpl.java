package com.retrieval.data;

import com.retrieval.DB;

import java.io.IOException;
import java.util.Stack;

public class DBImpl implements DB {


    @Override
    public void put(byte[] key, byte[] value) {

    }

    @Override
    public void delete(byte[] key) {
        Stack<Integer> st = new Stack<Integer>();
    }

    @Override
    public void close() throws IOException {

    }
}
