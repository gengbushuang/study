package raft.rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.RaftMessageHandler;
import raft.RaftMessageType;
import raft.RaftRequestMessage;
import raft.RaftResponseMessage;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by gbs on 19/3/27.
 */
public class RaftMessageHandlerTest implements RaftMessageHandler {

    private Random random = new Random(Calendar.getInstance().getTimeInMillis());

    private Logger logger = LogManager.getLogger(getClass());

    @Override
    public RaftResponseMessage processRequest(RaftRequestMessage request) {
        String log = String.format(
                "Receive a request(Source: %d, Destination: %d, Term: %d, LLI: %d, LLT: %d, CI: %d, LEL: %d",
                request.getSource(),
                request.getDestination(),
                request.getTerm(),
                request.getLastLogIndex(),
                request.getLastLogTerm(),
                request.getCommitIndex(),
                request.getLogEntries() == null ? 0 : request.getLogEntries().length);
        logger.debug(log);
        System.out.println(log);


        RaftResponseMessage response = null;
        if (request.getMessageType() == RaftMessageType.RequestVoteRequest) {
            response = handleVoteRequest(request);
        } else if (request.getMessageType() == RaftMessageType.AppendEntriesRequest) {
            response = handlerAppendEntriesRequest(request);
        } else {
            System.out.println(request);
        }
        return response;
    }


    private RaftResponseMessage handleVoteRequest(RaftRequestMessage request) {
        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.RequestVoteResponse);
        response.setGranted(this.random.nextBoolean());
        response.setDestination(request.getSource());
        response.setSource(this.random.nextInt());
        response.setTerm(request.getTerm());
        response.setNextIndex(this.random.nextLong());
        return response;

    }

    private RaftResponseMessage handlerAppendEntriesRequest(RaftRequestMessage request) {
        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.AppendEntriesResponse);
        response.setGranted(this.random.nextBoolean());
        response.setDestination(request.getSource());
        response.setSource(this.random.nextInt());
        response.setTerm(request.getTerm());
        response.setNextIndex(this.random.nextLong());
        return response;
    }
}
