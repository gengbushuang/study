package raft;

public class RaftOptions {

    //选举时间上界,最大时间
    private int electionTimeoutUpperBound;
    //选举时间下界,最小时间
    private int electionTimeoutLowerBound;

    private int heartbeatInterval;

    public int getElectionTimeoutUpperBound() {
        return electionTimeoutUpperBound;
    }

    public void setElectionTimeoutUpperBound(int electionTimeoutUpperBound) {
        this.electionTimeoutUpperBound = electionTimeoutUpperBound;
    }

    public int getElectionTimeoutLowerBound() {
        return electionTimeoutLowerBound;
    }

    public void setElectionTimeoutLowerBound(int electionTimeoutLowerBound) {
        this.electionTimeoutLowerBound = electionTimeoutLowerBound;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }
}
