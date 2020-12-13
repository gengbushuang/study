package com.retrieval.constants;

import com.retrieval.util.ByteSizeUtils;

public class LogConstants {

    public static final int LOG_BLOCK_SIZE = 32768;

    //4+2+1
//    public static final int LOG_HEADER_SIZE = ByteSizeUtils.INT_SIZE + ByteSizeUtils.SHORT_SIZE + ByteSizeUtils.BYTE_SIZE;

    public static final int LOG_HEADER_SIZE = ByteSizeUtils.INT_SIZE + ByteSizeUtils.BYTE_SIZE;

    private LogConstants() {
    }
}
