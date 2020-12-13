package com.retrieval.data;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MappedLogWriterTest {

    @Test
    public void testWriter() throws IOException {
        File databaseDir = new File("example");
        databaseDir.mkdirs();

        File file = new File(databaseDir, "001.log");

        MappedLogWriter logWriter = new MappedLogWriter(file, 1);
        for (int i = 0; i < 1000; i++) {
            DataByte dataByte = DataBytes.wrappedBuffer("fffffffffffffffffffffgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg".getBytes());

            logWriter.addRecord(dataByte, false);
        }
        logWriter.close();


    }
}
