package raft.message;

public enum LogValueType {
    //
//    PING,
//    GENERAL;
    ERROR(-1),
    PING(0),
    GENERAL(1);

    private final int code;

    private LogValueType(int i) {
        this.code = i;
    }

    public int getCode() {
        return code;
    }


    public static LogValueType ofKey(int type) {
        for (LogValueType logValueType : LogValueType.values()) {
            if (logValueType.getCode() == type) {
                return logValueType;
            }
        }
        return ERROR;
    }
}