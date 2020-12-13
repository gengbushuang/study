package raft.message.request;

import java.util.Arrays;

public class GeneralEntry extends AbstractEntry {

    private final byte[] value;

    public GeneralEntry(int type, int index, int term, byte[] value) {
        super(type, index, term);
        this.value = value;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GeneralEntry{" +
                "index=" + this.getIndex() +
                ", term=" + this.getTerm() +
                "}";
    }
}
