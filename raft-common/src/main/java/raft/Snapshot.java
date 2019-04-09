package raft;

/**
 * Created by gbs on 19/3/26.
 */
public class Snapshot {
    //下标
    private long lastLogIndex;
    //任期号
    private long lastLogTerm;

    private long size;

    private ClusterConfiguration lastConfig;

    public Snapshot(long lastLogIndex, long lastLogTerm, ClusterConfiguration lastConfig){
        this(lastLogIndex, lastLogTerm, lastConfig, 0);
    }

    public Snapshot(long lastLogIndex, long lastLogTerm, ClusterConfiguration lastConfig, long size){
        this.lastConfig = lastConfig;
        this.lastLogIndex = lastLogIndex;
        this.lastLogTerm = lastLogTerm;
        this.size = size;
    }

    public long getLastLogIndex() {
        return lastLogIndex;
    }

    public long getLastLogTerm() {
        return lastLogTerm;
    }

    public ClusterConfiguration getLastConfig() {
        return lastConfig;
    }

    public long getSize(){
        return this.size;
    }

}
