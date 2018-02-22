package org.raft.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.raft.common.message.LogEntry;
import org.raft.common.message.RaftMessageType;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;

public class BinaryUtils {

	public static final int RAFT_REQUEST_HEADER_SIZE = Integer.BYTES * 3 + Long.BYTES * 4 + 1;
	public static final int RAFT_RESPONSE_HEADER_SIZE = Integer.BYTES * 2 + Long.BYTES * 2 + 2;

	// long转换byte
	public static byte[] longToBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(value);
		return buffer.array();
	}

	// byte转换long
	public static long bytesToLong(byte[] bytes, int offset) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, Long.BYTES);
		return buffer.getLong();
	}

	// int转换byte
	public static byte[] intToBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(value);
		return buffer.array();
	}

	// byte转换int
	public static int bytesToInt(byte[] bytes, int offset) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, Integer.BYTES);
		return buffer.getInt();
	}

	// boolean转换byte
	public static byte booleanToByte(boolean value) {
		return value ? (byte) 1 : (byte) 0;
	}

	// byte转换boolean
	public static boolean byteToBoolean(byte value) {
		return value != 0;
	}

	// RaftRequestMessage转换成byte
	public static byte[] messageToBytes(RaftRequestMessage request) {
		LogEntry[] logEntries = request.getLogEntries();
		int logSize = 0;
		List<byte[]> buffersForLogs = null;
		if (logEntries != null && logEntries.length > 0) {
			buffersForLogs = new ArrayList<byte[]>(logEntries.length);
			for (LogEntry logEntry : logEntries) {
				byte[] logData = logEntryToBytes(logEntry);
				logSize += logData.length;
				buffersForLogs.add(logData);
			}
		}

		ByteBuffer requestBuffer = ByteBuffer.allocate(RAFT_REQUEST_HEADER_SIZE + logSize);
		// 1位
		requestBuffer.put(request.getMessageType().toByte());
		// 4位
		requestBuffer.put(intToBytes(request.getSource()));
		// 4位
		requestBuffer.put(intToBytes(request.getDestination()));

		// 8位
		requestBuffer.put(longToBytes(request.getTerm()));
		// 8位
		requestBuffer.put(longToBytes(request.getLastLogTerm()));
		// 8位
		requestBuffer.put(longToBytes(request.getLastLogIndex()));
		// 8位
		requestBuffer.put(longToBytes(request.getCommitIndex()));
		// 4位
		requestBuffer.put(intToBytes(logSize));
		if (buffersForLogs != null) {
			for (byte[] bs : buffersForLogs) {
				requestBuffer.put(bs);
			}
		}
		return requestBuffer.array();
	}

	// byte转换RaftRequestMessage对象
	public static Pair<RaftRequestMessage, Integer> bytesToRequestMessage(byte[] data) {
		if (data == null || data.length != RAFT_REQUEST_HEADER_SIZE) {
			throw new IllegalArgumentException("invalid request message header.");
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		RaftRequestMessage requestMessage = new RaftRequestMessage();
		// 1位
		requestMessage.setMessageType(RaftMessageType.fromByte(buffer.get()));
		// 4位
		requestMessage.setSource(buffer.getInt());
		// 4位
		requestMessage.setDestination(buffer.getInt());
		// 8位
		requestMessage.setTerm(buffer.getLong());
		// 8位
		requestMessage.setLastLogTerm(buffer.getLong());
		// 8位
		requestMessage.setLastLogIndex(buffer.getLong());
		// 8位
		requestMessage.setCommitIndex(buffer.getLong());
		int logDataSize = buffer.getInt();
		return new Pair<RaftRequestMessage, Integer>(requestMessage, logDataSize);
	}

	// logEntry转换byte
	public static byte[] logEntryToBytes(LogEntry logEntry) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			// 8位
			output.write(longToBytes(logEntry.getTerm()));
			// output.write(logEntry.getValueType().toByte());
			// 4位
			output.write(intToBytes(logEntry.getValue().length));
			output.write(logEntry.getValue());
			output.flush();
			return output.toByteArray();
		} catch (IOException exception) {
			throw new RuntimeException("logEntry对象转换byte出现异常");
		}
	}

	// byte转换LogEntry对象
	public static LogEntry[] bytesToLogEntries(byte[] data) {
		if (data == null || data.length < Long.BYTES) {
			throw new IllegalArgumentException("invalid log entries data");
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		List<LogEntry> logEntries = new ArrayList<LogEntry>();
		while (buffer.hasRemaining()) {
			long term = buffer.getLong();
			// byte valueType = buffer.get();
			int valueSize = buffer.getInt();
			byte[] value = new byte[valueSize];
			if (valueSize > 0) {
				buffer.get(value);
			}
			logEntries.add(new LogEntry(term, value));
		}
		return logEntries.toArray(new LogEntry[0]);
	}

	// RaftResponseMessage对象转换byte
	public static byte[] messageToBytes(RaftResponseMessage response) {
		ByteBuffer responseBuffer = ByteBuffer.allocate(RAFT_RESPONSE_HEADER_SIZE);
		// 1位
		responseBuffer.put(response.getMessageType().toByte());
		// 4位
		responseBuffer.put(intToBytes(response.getSource()));
		// 4位
		responseBuffer.put(intToBytes(response.getDestination()));
		// 8位
		responseBuffer.put(longToBytes(response.getTerm()));
		// 8位
		responseBuffer.put(longToBytes(response.getNextIndex()));
		// 1位
		responseBuffer.put(booleanToByte(response.isAccepted()));
		return responseBuffer.array();
	}

	// byte转换RaftResponseMessage对象
	public static RaftResponseMessage bytesToResponseMessage(byte[] data) {
		if (data == null || data.length != RAFT_RESPONSE_HEADER_SIZE) {
			throw new IllegalArgumentException(String.format("data must have %d bytes for a raft response message", RAFT_RESPONSE_HEADER_SIZE));
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		RaftResponseMessage response = new RaftResponseMessage();
		// 1位
		response.setMessageType(RaftMessageType.fromByte(buffer.get()));
		// 4位
		response.setSource(buffer.getInt());
		// 4位
		response.setDestination(buffer.getInt());
		// 8位
		response.setTerm(buffer.getLong());
		// 8位
		response.setNextIndex(buffer.getLong());
		// 1位
		response.setAccepted(buffer.get() == 1);
		return response;
	}
}
