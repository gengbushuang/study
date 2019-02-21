package raft;

public class LogEntry {
    private byte[] value;
    private long term;
    private LogValueType vaueType;

    public LogEntry(){
        this(0,null);
    }

    public LogEntry(long term, byte[] value) {
        this(term, value, LogValueType.Application);
    }

    public LogEntry(long term, byte[] value, LogValueType valueType) {
        this.term = term;
        this.value = value;
        this.vaueType = valueType;
    }

    public byte[] getValue() {
        return value;
    }

    public long getTerm() {
        return term;
    }

    public LogValueType getVaueType() {
        return vaueType;
    }
}
