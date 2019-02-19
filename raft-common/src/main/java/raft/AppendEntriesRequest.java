package raft;

public class AppendEntriesRequest {
    //领导人的ID
    private int serverId;
    //领导人的任期号
    private long term;
    //新的日志条目紧随之前的索引值
    private long prevLogIndex;
    //prevLogIndex条目的任期号
    private long predLogTerm;
    //领导人已提交的日志索引
    private long commitIndex;
}
