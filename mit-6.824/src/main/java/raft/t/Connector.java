package raft.t;

import raft.t.msg.AppendEntriesResult;
import raft.t.msg.AppendEntriesRpc;
import raft.t.msg.RequestVoteRpc;
import raft.t.msg.RequestVoteResult;

import java.util.Collection;

public interface Connector {

    void init();

    void sendRequestVote(RequestVoteRpc rpc, Collection<NodeEndpoint> nodeEndpoints);

    void replyRequestVote(RequestVoteResult result,NodeEndpoint endpoint);

    void sendAppendEntries(AppendEntriesRpc rpc,NodeEndpoint endpoint);

    void replyAppendEntries(AppendEntriesResult result,NodeEndpoint endpoint);

    void close();
}
