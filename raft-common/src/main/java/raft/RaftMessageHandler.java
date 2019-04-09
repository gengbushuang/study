package raft;

public interface RaftMessageHandler {

    public RaftResponseMessage processRequest(RaftRequestMessage request);
}