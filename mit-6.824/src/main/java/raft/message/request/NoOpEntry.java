package raft.message.request;

import raft.message.LogValueType;

public class NoOpEntry extends AbstractEntry {

    public NoOpEntry(int index, int term) {
        super(LogValueType.PING.getCode(), index, term);
    }

    @Override
    public byte[] getValue() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "NoOpEntry{" +
                "index=" + this.getIndex() +
                ", term=" + this.getTerm() +
                "}";
    }
}
