package raft.desc;

/**
 * 由Candidate调起以拉取选票
 */
public class RequestVote {
    class RequestVoteRequest{
        //Candidate的Term ID
        private long term;
        //Candidate的ID
        private long candidateId;
        //Candidate所持有的最后一条日志记录的index
        private long lastLogIndex;
        //Candidate所持有的最后一条日志记录的Term ID
        private long lastLogTerm;
    }

    class RequestVoteResponse{
        //接收方的Term ID
        private long term;
        //接收方是否同意给出的选票
        private boolean voteGranted;
    }

    /**
     * 接收方在接收到该RPC后会进行以下操作:
     * 1.若term<currentTerm,返回false
     * 2.若votedFor==null且给定的日志记录信息可得出对方的日志和自己的相同甚至更新,返回true
     */
}
