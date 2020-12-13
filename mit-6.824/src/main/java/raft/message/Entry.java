package raft.message;

public interface Entry {

    public int getType();

    public int getIndex();

    public int getTerm();

    public byte[] getValue();
}
