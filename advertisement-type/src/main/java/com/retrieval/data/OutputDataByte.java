package com.retrieval.data;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputDataByte extends OutputStream implements DataOutput {


    @Override
    public abstract void writeBoolean(boolean v);
    @Override
    public abstract void writeByte(int v);
    @Override
    public abstract void writeShort(int v);

    @Override
    public abstract void writeInt(int v);

    @Override
    public abstract void writeLong(long v);


    //////////////////////////////////////////////////////


    @Override
    public void writeChar(int v) throws IOException {

    }

    @Override
    public void writeFloat(float v) throws IOException {

    }

    @Override
    public void writeDouble(double v) throws IOException {

    }

    @Override
    public void writeBytes(String s) throws IOException {

    }

    @Override
    public void writeChars(String s) throws IOException {

    }

    @Override
    public void writeUTF(String s) throws IOException {

    }
}
