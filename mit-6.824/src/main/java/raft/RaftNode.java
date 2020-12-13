package raft;

import raft.message.request.AppendEntriesRequest;
import raft.message.response.AppendEntriesResponse;

public interface RaftNode {

    public int getTerm();

    public void setLeaderId(int id);

    public int getServiceId();

    public RaftState getState();

    public ServerRole getRole();

    void updateTerm(int term);

    void updateVoteFor(int serverId);

    void resetElection();

    void becomeFollower();

    void becomeLeader();

    boolean compareLog(int lastLogTerm, int lastLogIndex);

    AppendEntriesResponse handleAppendEntriesRequest(AppendEntriesRequest appendEntriesRequest);


}
