package raft;

import raft.message.request.AppendEntriesRequest;
import raft.message.request.RequestVoteRequest;
import raft.message.response.AppendEntriesResponse;
import raft.message.response.RequestVoteResponse;

public interface RaftNodeService {

    RequestVoteResponse sendVote(RequestVoteRequest requestVoteRequest);

    AppendEntriesResponse sendEntrie(AppendEntriesRequest appendEntriesRequest);
}
