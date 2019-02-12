package raft;

public interface Message {

    byte[] write();

    void reads(byte [] out);
}
