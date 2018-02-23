package org.raft.common.exception;

import org.raft.common.message.RaftRequestMessage;

public class RpcException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6109905778709360017L;

	private RaftRequestMessage request;

	public RpcException(Throwable realException, RaftRequestMessage request) {
		super(realException.getMessage(), realException);
		this.request = request;
	}

	public RaftRequestMessage getRequest() {
		return request;
	}

}
