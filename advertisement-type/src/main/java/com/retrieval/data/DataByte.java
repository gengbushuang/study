package com.retrieval.data;

import com.retrieval.util.ByteSizeUtils;
import com.retrieval.util.CheckUtils;

import java.nio.ByteBuffer;

public class DataByte {
    //数据
    private final byte[] data;
    //数据偏移位置
    private final int offset;
    //数据长度
    private final int length;

    public DataByte(int length) {
        this(new byte[length], 0, length);
    }

    public DataByte(byte[] data) {
//        this.data = data;
//        this.offset = 0;
//        this.length = data.length;
        this(data, 0, data.length);
    }

    public DataByte(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public byte[] getDataArray() {
        return data;
    }

    public DataByte copyData() {
        return this.copyData(0, this.length);
    }

    public DataByte copyData(int index, int length) {
        byte[] copyData = new byte[length];
        index += this.offset;
        System.arraycopy(data, index, copyData, 0, length);
        return new DataByte(copyData);
    }

    public void writeByte(int index, int v) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.BYTE_SIZE, this.length);

        index += this.offset;
        data[index] = (byte) (v >>> 0);
    }

    public void writeShort(int index, int v) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.SHORT_SIZE, this.length);

        index += this.offset;
        data[index] = (byte) (v >>> 0);
        data[index + 1] = (byte) (v >>> 8);
    }

    public void writeInt(int index, int v) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.INT_SIZE, this.length);

        index += this.offset;
        data[index] = (byte) (v >>> 0);
        data[index + 1] = (byte) (v >>> 8);
        data[index + 2] = (byte) (v >>> 16);
        data[index + 3] = (byte) (v >>> 24);
    }

    public void writeLong(int index, long v) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.LONG_SIZE, this.length);

        index += this.offset;
        data[index] = (byte) (v >>> 0);
        data[index + 1] = (byte) (v >>> 8);
        data[index + 2] = (byte) (v >>> 16);
        data[index + 3] = (byte) (v >>> 24);
        data[index + 4] = (byte) (v >>> 32);
        data[index + 5] = (byte) (v >>> 40);
        data[index + 6] = (byte) (v >>> 48);
        data[index + 7] = (byte) (v >>> 56);

    }

    public void writeBytes(int index, byte[] b, int off, int len) {
        CheckUtils.checkPositionIndexes(index, index + len, this.length);

        index += this.offset;
        System.arraycopy(b, off, data, index, length);
    }

    public byte getByte(int index) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.BYTE_SIZE, this.length);

        index += this.offset;
        return data[index];
    }

    public void getBytes(int index, ByteBuffer buffer) {
        CheckUtils.checkPositionIndex(index, this.length);

        index += this.offset;
        buffer.put(data, index, Math.min(this.length, buffer.remaining()));
    }

    public void getBytes(int index, byte[] dataDest, int dataIndex, int dataLength) {
        CheckUtils.checkPositionIndexes(index, index + dataLength, this.length);


        index += this.offset;
        System.arraycopy(data, index, dataDest, dataIndex, dataLength);
    }

    public short getShort(int index) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.SHORT_SIZE, this.length);

        index += this.offset;
        return (short) (data[index] & 0xFF |
                data[index + 1] << 8);

    }

    public int getInt(int index) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.INT_SIZE, this.length);

        index += this.offset;
        return (data[index] & 0xFF) |
                (data[index + 1] & 0xFF) << 8 |
                (data[index + 2] & 0xFF) << 16 |
                (data[index + 3] & 0xFF) << 24;
    }

    public long getLong(int index) {
        CheckUtils.checkPositionIndexes(index, index + ByteSizeUtils.LONG_SIZE, this.length);

        index += this.offset;
        return ((long) data[index] & 0xFF) |
                ((long) data[index + 1] & 0xFF) << 8 |
                ((long) data[index + 2] & 0xFF) << 16 |
                ((long) data[index + 3] & 0xFF) << 24 |
                ((long) data[index + 4] & 0xFF) << 32 |
                ((long) data[index + 5] & 0xFF) << 40 |
                ((long) data[index + 6] & 0xFF) << 48 |
                ((long) data[index + 7] & 0xFF) << 56;
    }

    public DataByte getDataByte(int index, int len) {

        CheckUtils.checkPositionIndexes(index, index + len, this.length);

        return new DataByte(data, index + this.offset, len);
    }


    public OutputDataByte output() {
        return new BasicOutputData(this);
    }

    public InputDataByte input() {
        return new BasicInputData(this);
    }


}
