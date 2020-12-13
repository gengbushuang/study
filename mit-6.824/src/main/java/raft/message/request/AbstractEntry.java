package raft.message.request;

import raft.message.Entry;

abstract class AbstractEntry implements Entry {

    private final int type;
    private final int index;
    private final int term;

    AbstractEntry(int type,int index,int term){
        this.type = type;
        this.index = index;
        this.term = term;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getTerm() {
        return term;
    }
}
