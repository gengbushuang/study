package com.retrieval;

import java.io.Closeable;

public interface DB extends Closeable {

    public void put(byte[] key, byte[] value);

    public void delete(byte[] key);
}
