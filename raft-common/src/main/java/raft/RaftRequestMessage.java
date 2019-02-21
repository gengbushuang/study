package raft;

public class RaftRequestMessage extends RaftMessage {

    private long lastLogTerm;//候选人的最后日志条目的任期号
    private long lastLogIndex;//候选人最后日志条目的索引值
    //领导人已提交的日志索引
    private long commitIndex;
}
