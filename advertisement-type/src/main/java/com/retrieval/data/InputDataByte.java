package com.retrieval.data;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputDataByte extends InputStream implements DataInput {

    public abstract void readBytes(byte[] data);

    public abstract void readBytes(byte[] data, int index, int length);

    public abstract DataByte readBytes(int len);


    @Override
    public abstract int available();

    public boolean isReadable() {
        return available() > 0;
    }
}
