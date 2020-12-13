package com.retrieval.util;


import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

public class CheckUtils {

    public static <T> T checkNonNull(T t, String message) {
        return Objects.requireNonNull(t, message);
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > end || end > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start >= 0 && start <= size) {
            return end >= 0 && end <= size ? StringUtils.format("end index (%s) must > start index (%s)", new Object[]{end, start}) : badPositionIndex(end, size, "end index");
        } else {
            return badPositionIndex(start, size, "start index");
        }
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return StringUtils.format("%s (%s) must not be negative", new Object[]{desc, index});
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return StringUtils.format("%s (%s) must not be greater than size (%s)", new Object[]{desc, index, size});
        }
    }

    public static void checkPositionIndex(int index, int size) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, "index"));
        }
    }
}
