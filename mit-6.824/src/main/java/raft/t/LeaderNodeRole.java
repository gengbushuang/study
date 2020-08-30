package raft.t;

public class LeaderNodeRole extends AbstractNodeRole {
    LeaderNodeRole(ServerRole name, int term) {
        super(ServerRole.Leader, term);
    }

    @Override
    public void cancelTimeoutOrTask() {

    }
}
