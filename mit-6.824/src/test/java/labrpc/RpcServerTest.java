package labrpc;

import labrpc.impl.RpcServer;
import raft.RaftNodeService;
import raft.RaftNodeServiceImpl;
import raft.message.request.AppendEntriesRequest;
import raft.message.request.RequestVoteRequest;
import raft.message.response.AppendEntriesResponse;
import raft.message.response.RequestVoteResponse;

public class RpcServerTest {
    public static void main(String[] args) throws InterruptedException {
        RpcServer rpcServer = new RpcServer(12345);
        rpcServer.register(new RaftNodeService() {
            @Override
            public RequestVoteResponse sendVote(RequestVoteRequest requestVoteRequest) {
                return new RequestVoteResponse();
            }

            @Override
            public AppendEntriesResponse sendEntrie(AppendEntriesRequest appendEntriesRequest) {
                return new AppendEntriesResponse();
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
    }
}
