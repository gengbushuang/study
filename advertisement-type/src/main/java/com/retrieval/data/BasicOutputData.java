package com.retrieval.data;

import java.io.IOException;

public class BasicOutputData extends OutputDataByte {

    private final DataByte dataByte;

    private int size;

    public BasicOutputData(DataByte dataByte) {
        this.dataByte = dataByte;
        this.size = 0;
    }

    private void incCount(int value) {
        int tmp = this.size + value;
        this.size = tmp;
    }

    @Override
    public void write(int b) throws IOException {
        this.writeByte(b);
    }

    @Override
    public void write(byte[] b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        dataByte.writeBytes(size, b, off, len);
        this.incCount(len);
    }

    @Override
    public void writeBoolean(boolean v) {
        this.writeByte(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) {
        dataByte.writeByte(size, v);
        this.incCount(1);
    }

    @Override
    public void writeShort(int v) {
        dataByte.writeShort(size, v);
        this.incCount(2);
    }

    @Override
    public void writeInt(int v) {
        dataByte.writeInt(size, v);
        this.incCount(4);
    }

    @Override
    public void writeLong(long v) {
        dataByte.writeLong(size, v);
        this.incCount(8);
    }


}
