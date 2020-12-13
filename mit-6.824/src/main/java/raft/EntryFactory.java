package raft;

import raft.message.Entry;
import raft.message.LogValueType;
import raft.message.request.GeneralEntry;
import raft.message.request.NoOpEntry;

import static raft.message.LogValueType.*;

public class EntryFactory {

    public Entry create(int type, int index, int term, byte[] values) {

        switch (LogValueType.ofKey(type)) {
            case PING:
                return new NoOpEntry(index, term);
            case GENERAL:
                return new GeneralEntry(type, index, term, values);
            default:
                throw new IllegalArgumentException("logtype error!type=" + type);

        }
    }
}
