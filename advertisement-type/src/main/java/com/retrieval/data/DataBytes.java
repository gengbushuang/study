package com.retrieval.data;

public class DataBytes {


    public static final DataByte EMPTY = new DataByte(0);

    private DataBytes() {
    }

    public static DataByte wrappedBuffer(byte[] array) {
        if (array.length == 0) {
            return EMPTY;
        }

        return new DataByte(array);
    }
}
