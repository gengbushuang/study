package org.raft.common.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.raft.common.message.RaftMessageHandler;
import org.raft.common.message.RaftMessageType;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;

public class TestMessage implements RaftMessageHandler {

	private Logger logger;

	public TestMessage() {
		this.logger = LogManager.getLogger(getClass());
	}

	@Override
	public RaftResponseMessage processRequest(RaftRequestMessage request) {
		this.logger.debug("Receive a %s message from %d with LastLogIndex=%d, LastLogTerm=%d, EntriesLength=%d, CommitIndex=%d and Term=%d", request.getMessageType().toString(), request.getSource(),
				request.getLastLogIndex(), request.getLastLogTerm(), request.getLogEntries() == null ? 0 : request.getLogEntries().length, request.getCommitIndex(), request.getTerm());
		RaftResponseMessage response = null;
		if (request.getMessageType() == RaftMessageType.AppendEntriesRequest) {
			response = this.handleAppendEntriesRequest(request);
		} else if (request.getMessageType() == RaftMessageType.RequestVoteRequest) {
			response = this.handleVoteRequest(request);
		} else if (request.getMessageType() == RaftMessageType.ClientRequest) {
			response = this.handleClientRequest(request);
		} else {
			response = this.handleExtendedMessages(request);
		}
		if (response != null) {
			this.logger.debug("Response back a %s message to %d with Accepted=%s, Term=%d, NextIndex=%d", response.getMessageType().toString(), response.getDestination(),
					String.valueOf(response.isAccepted()), response.getTerm(), response.getNextIndex());
		}

		return response;
	}

	private synchronized RaftResponseMessage handleAppendEntriesRequest(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.AppendEntriesResponse);
		response.setNextIndex(2);
		return response;
	}

	private synchronized RaftResponseMessage handleVoteRequest(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.RequestVoteResponse);
		response.setNextIndex(1);
		return response;
	}

	private RaftResponseMessage handleClientRequest(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.ClientRequest);
		response.setNextIndex(3);
		return response;
	}

	private synchronized RaftResponseMessage handleExtendedMessages(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.LeaveClusterResponse);
		response.setNextIndex(4);
		return response;
	}
}
