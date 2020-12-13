package raft.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.RaftContext;
import raft.RaftNode;
import raft.RaftNodeService;
import raft.message.request.AppendEntriesRequest;
import raft.message.request.RequestVoteRequest;
import raft.message.response.AppendEntriesResponse;
import raft.message.response.RequestVoteResponse;

public class RaftNodeServiceImpl implements RaftNodeService {

    private Logger logger = LogManager.getLogger(getClass());

    private RaftNode raftNode;

    public RaftNodeServiceImpl(RaftContext context) {
        this.raftNode = new RaftNodeImpl(context);
    }

    @Override
    public RequestVoteResponse sendVote(RequestVoteRequest requestVoteRequest) {
        this.logger.info("Receive a {} ", requestVoteRequest);

        RequestVoteResponse voteResponse = new RequestVoteResponse();
        voteResponse.setDestination(requestVoteRequest.getSource());
        voteResponse.setSource(raftNode.getServiceId());
        //发送过来的任期比自己的小
        if (requestVoteRequest.getTerm() < raftNode.getTerm()) {
            voteResponse.setTerm(raftNode.getTerm());
            voteResponse.setGranted(false);
            voteResponse.setLogOk(false);
            return voteResponse;
        }


        //发送过来的任期比自己的大
        if (requestVoteRequest.getTerm() > raftNode.getTerm()) {
            //如果发送过来的term比自己的大，会自动更新term，并且自动转换为Follower角色
            raftNode.updateTerm(requestVoteRequest.getTerm());
            raftNode.updateVoteFor(requestVoteRequest.getSource());
            voteResponse.setTerm(raftNode.getTerm());
            voteResponse.setGranted(true);
            voteResponse.setLogOk(true);
            return voteResponse;
        }

        boolean isLogOk = raftNode.compareLog(requestVoteRequest.getLastLogTerm(), requestVoteRequest.getLastLogIndex());
        //发过来的任期跟自己一样
        switch (raftNode.getRole()) {
            case Follower:
                //比较日志
                long votedFor = raftNode.getState().getVotedFor();
                voteResponse.setTerm(raftNode.getTerm());
                if (isLogOk && votedFor == -1 || requestVoteRequest.getSource() == votedFor) {
                    raftNode.updateVoteFor(requestVoteRequest.getSource());
                    voteResponse.setGranted(true);
                    voteResponse.setLogOk(true);
                    return voteResponse;
                }
                voteResponse.setLogOk(false);
                voteResponse.setGranted(false);
                return voteResponse;
            case Candidate:
            case Leader:
                voteResponse.setTerm(raftNode.getTerm());
                voteResponse.setGranted(false);
                voteResponse.setLogOk(false);
                return voteResponse;
            default:
                throw new IllegalStateException("raft role [" + raftNode.getRole() + "]");
        }
    }

    @Override
    public AppendEntriesResponse sendEntrie(AppendEntriesRequest appendEntriesRequest) {
        this.logger.info("Receive a {} ", appendEntriesRequest);

        AppendEntriesResponse entriesResponse = new AppendEntriesResponse();
        entriesResponse.setDestination(appendEntriesRequest.getSource());
        entriesResponse.setSource(raftNode.getServiceId());

        //发送过来的term比自己小，则回复自己的term
        if (appendEntriesRequest.getTerm() < raftNode.getTerm()) {
            entriesResponse.setTerm(raftNode.getTerm());
            entriesResponse.setSuccess(false);
            return entriesResponse;
        }

        //发送过来的term比自己大，则转化Follower角色
        if (appendEntriesRequest.getTerm() > raftNode.getTerm()) {
            //如果发送过来的term比自己的大，会自动更新term，并且自动转换为Follower角色
            raftNode.updateTerm(appendEntriesRequest.getTerm());
            return raftNode.handleAppendEntriesRequest(appendEntriesRequest);
        }

        switch (raftNode.getRole()) {
            case Follower:
                raftNode.resetElection();
                return raftNode.handleAppendEntriesRequest(appendEntriesRequest);
            case Candidate:
                //降级跟随者
                raftNode.becomeFollower();
                return raftNode.handleAppendEntriesRequest(appendEntriesRequest);
            case Leader:
                //自己的角色是Leader要打印警告消息
                this.logger.error("Receive AppendEntriesRequest role is leader {}", appendEntriesRequest.getSource());
                entriesResponse.setTerm(raftNode.getTerm());
                entriesResponse.setSuccess(false);
                return entriesResponse;
            default:
                throw new IllegalStateException("raft role [" + raftNode.getRole() + "]");
        }
    }

}
