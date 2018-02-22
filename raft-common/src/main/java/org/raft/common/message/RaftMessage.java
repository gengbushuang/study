package org.raft.common.message;

public class RaftMessage {

	private RaftMessageType messageType;
	// 来源
	private int source;
	// 目标
	private int destination;
	// 任期
	private long term;

	public RaftMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(RaftMessageType messageType) {
		this.messageType = messageType;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public long getTerm() {
		return term;
	}

	public void setTerm(long term) {
		this.term = term;
	}

	@Override
	public String toString() {
		return "RaftMessage [messageType=" + messageType + ", source=" + source + ", destination=" + destination + ", term=" + term + "]";
	}
	
}
