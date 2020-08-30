package raft;

import raft.message.request.AppendEntriesRequest;
import raft.message.response.AppendEntriesResponse;
import raft.t.ServerRole;

public interface RaftNode {

    public long getTerm();

    public void setLeaderId(int id);

    public int getServiceId();

    public RaftState getState();

    public ServerRole getRole();

    void updateTerm(long term);

    void updateVoteFor(long serverId);

    void resetElection();

    void becomeFollower();

    void becomeLeader();

    AppendEntriesResponse handleAppendEntriesRequest(AppendEntriesRequest appendEntriesRequest);
}
