package com.retrieval.data;

import java.io.IOException;

public class BasicInputData extends InputDataByte {

    private final DataByte dataByte;

    private int position;

    public BasicInputData(DataByte dataByte) {
        this.dataByte = dataByte;
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        this.readBytes(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        this.readBytes(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return 0;
    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public byte readByte() throws IOException {
        byte v = dataByte.getByte(position);
        position += 1;
        return v;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return 0;
    }

    @Override
    public short readShort() throws IOException {
        short v = dataByte.getShort(position);
        position += 2;
        return v;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return 0;
    }

    @Override
    public char readChar() throws IOException {
        return 0;
    }

    @Override
    public int readInt() throws IOException {
        int v = dataByte.getInt(position);
        position += 4;
        return v;
    }

    @Override
    public long readLong() throws IOException {
        long v = dataByte.getLong(position);
        position += 8;
        return v;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public String readLine() throws IOException {
        return null;
    }

    @Override
    public String readUTF() throws IOException {
        return null;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    /**
     * 还剩余空间
     *
     * @return
     * @throws IOException
     */
    @Override
    public int available() {
        return dataByte.getLength() - position;
    }


    @Override
    public void readBytes(byte[] data) {
        this.readBytes(data, 0, data.length);
    }

    @Override
    public void readBytes(byte[] data, int index, int length) {

        dataByte.getBytes(position, data, index, length);
        position += length;
    }

    @Override
    public DataByte readBytes(int len) {

        DataByte value = dataByte.getDataByte(position, len);
        position+=len;
        return value;
    }
}
