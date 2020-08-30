package raft.t;

public class CandidateNodeRole extends AbstractNodeRole {

    private final int votesCount;
    private final ElectionTimeout electionTimeout;

    CandidateNodeRole(int term, ElectionTimeout electionTimeout) {
        this(term, 1, electionTimeout);
    }

    CandidateNodeRole(int term, int votesCount, ElectionTimeout electionTimeout) {
        super(ServerRole.Candidate, term);
        this.votesCount = votesCount;
        this.electionTimeout = electionTimeout;
    }

    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }

    public int getVotesCount() {
        return votesCount;
    }
}
