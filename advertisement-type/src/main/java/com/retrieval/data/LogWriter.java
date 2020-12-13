package com.retrieval.data;

import java.io.File;
import java.io.IOException;

public interface LogWriter {

    boolean isClosed();

    void close()
            throws IOException;

    void delete()
            throws IOException;

    File getFile();

    long getFileNumber();

    void addRecord(DataByte dataByte,boolean flush)
            throws IOException;
}
