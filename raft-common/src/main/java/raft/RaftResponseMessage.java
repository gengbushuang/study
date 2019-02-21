package raft;

public class RaftResponseMessage extends RaftMessage {

    private boolean granted;//候选人赢得了此张选票时为真
}
