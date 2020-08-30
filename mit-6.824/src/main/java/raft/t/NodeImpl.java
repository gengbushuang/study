package raft.t;

import raft.t.msg.RequestVoteResult;
import raft.t.msg.RequestVoteRpc;

public class NodeImpl implements Node {
    private final NodeContext context;

    private AbstractNodeRole nodeRole;

    public NodeImpl(NodeContext context){
        this.context = context;
    }
    @Override
    public synchronized void start() {

        context.getConnector().init();


    }

    @Override
    public void stop() throws InterruptedException {

    }

    void electTimeout(){
        context.getTaskExecutor().submit(()->this.doProcessElectionTimeout());
    }

    private void doProcessElectionTimeout(){
        if(nodeRole.getName()==ServerRole.Leader){
            return;
        }
       int newTerm =  nodeRole.getTrem()+1;
        nodeRole.cancelTimeoutOrTask();
//        this.changeToRole(new CandidateNodeRole(newTerm,));
        RequestVoteRpc rpc = new RequestVoteRpc();
        rpc.setTerm(newTerm);
        rpc.setCandidateId(context.getSelfId());
        rpc.setLastLogindex(0);
        rpc.setLastLogTerm(0);

        context.getConnector().sendRequestVote(rpc,context.getGroup().listEndpointExceptSelf());

    }

//    private RequestVoteResult doProcessRequestVoteRpc

    private void changeToRole(AbstractNodeRole newRole){

        NodeStore store = context.getStore();
        store.setTerm(newRole.getTrem());

        if(newRole.getName() == ServerRole.Follower){
            store.setVotedFor(((FollowerNodeRole)newRole).getVotedFor());
        }
        this.nodeRole = newRole;
    }

//    private ElectionTimeout scheduleElectionTimeout(){
//
//    }
}
