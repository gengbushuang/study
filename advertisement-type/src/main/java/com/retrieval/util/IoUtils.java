package com.retrieval.util;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {

    private IoUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
