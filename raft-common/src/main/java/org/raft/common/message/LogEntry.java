package org.raft.common.message;

import java.util.Arrays;

public class LogEntry {

	private byte[] value;
	private long term;

	public LogEntry() {
		this(0, null);
	}

	public LogEntry(long term, byte[] value) {
		this(term, value, null);
	}

	// valueType以后在添加
	public LogEntry(long term, byte[] value, Object valueType) {
		this.term = term;
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	public long getTerm() {
		return term;
	}

	@Override
	public String toString() {
		return "LogEntry [value=" + Arrays.toString(value) + ", term=" + term + "]";
	}
}
