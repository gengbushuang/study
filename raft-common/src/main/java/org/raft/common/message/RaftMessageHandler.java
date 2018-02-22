package org.raft.common.message;

public interface RaftMessageHandler {

	public RaftResponseMessage processRequest(RaftRequestMessage requestMessage);
}
