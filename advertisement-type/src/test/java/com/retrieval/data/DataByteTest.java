package com.retrieval.data;

import org.junit.Test;

import java.io.IOException;

public class DataByteTest {

    @Test
    public void testLong() throws IOException {
        DataByte dataByte = new DataByte(8);
        OutputDataByte output = dataByte.output();
        long l = 22222222222222L;
        output.writeLong(l);


        InputDataByte input = dataByte.input();
        byte[] data = dataByte.getDataArray();
        int index = 0;
        long tmp = (long) (data[index] & 0xFF) |
                (data[index + 1] & 0xFF) << 8 |
                (data[index + 2] & 0xFF) << 16 |
                (data[index + 3] & 0xFF) << 24 |
                (data[index + 4] & 0xFF) << 32 |
                (data[index + 5] & 0xFF) << 40 |
                (data[index + 6] & 0xFF) << 48 |
                (data[index + 7] & 0xFF) << 56;

        long readLong = input.readLong();
        System.out.println(readLong);
        System.out.println(tmp);
    }

    @Test
    public void testD(){
        byte[] bytes = "ssssssssssssssssssssssssssssssssssssssssssssssssdfsdfsdfsdfsdfdsfdsfdsfdsfsdfsdfdsfsf".getBytes();
        System.out.println(bytes.length);
    }
}
