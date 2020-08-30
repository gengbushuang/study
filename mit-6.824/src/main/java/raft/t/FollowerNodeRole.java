package raft.t;

public class FollowerNodeRole extends AbstractNodeRole {

    private final int votedFor;

    private final int leaderId;

    private final ElectionTimeout electionTimeout;

    FollowerNodeRole(int term,int votedFor,int leaderId,ElectionTimeout electionTimeout) {
        super(ServerRole.Follower, term);
        this.votedFor = votedFor;
        this.leaderId = leaderId;
        this.electionTimeout = electionTimeout;
    }

    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }

    public int getVotedFor() {
        return votedFor;
    }

    public int getLeaderId() {
        return leaderId;
    }
}
