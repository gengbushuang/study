package raft;

public class RaftRequestMessage extends RaftMessage {

    private long lastLogTerm;//候选人的最后日志条目的任期号
    private long lastLogIndex;//候选人最后日志条目的索引值
    //领导人已提交的日志索引
    private long commitIndex;

    private LogEntry[] logEntries;

    public long getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(long lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    public long getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(long lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public LogEntry[] getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(LogEntry[] logEntries) {
        this.logEntries = logEntries;
    }
}
