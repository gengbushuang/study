package labrpc;

import labrpc.impl.RpcClient;
import raft.RaftNodeService;
import raft.message.request.RequestVoteRequest;
import raft.message.response.RequestVoteResponse;

public class RpcClientTest {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("127.0.0.1",12345);

        RaftNodeService raftNodeService = rpcClient.getProxy(RaftNodeService.class);

        for(;;) {
            RequestVoteRequest voteRequest = new RequestVoteRequest();
            voteRequest.setTerm(1);
            voteRequest.setServerId(2);
            voteRequest.setLastLogIndex(3);
            voteRequest.setLastLogTerm(5);
            try {
                RequestVoteResponse voteResponse = raftNodeService.sendVote(voteRequest);

                System.out.println(voteResponse);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
