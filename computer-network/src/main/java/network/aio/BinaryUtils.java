package network.aio;

import java.nio.ByteBuffer;

import network.message.MessageType;
import network.message.RequestMessage;
import network.message.ResponseMessage;
import network.rdt.model.Packet;

public class BinaryUtils {

	public final static int REQUEST_HEAD_SIZE = Integer.BYTES + 1;

	public final static int RESPONSE_HEAD_SIZE = Integer.BYTES + 1;

	public final static int PACKET_HEAD_SIZE = Integer.BYTES * 2 + 2;

	public static byte[] requestToByte(RequestMessage message) {
		Packet packet = message.getPacket();
		byte[] packetToByte = packetToByte(packet);

		ByteBuffer buffer = ByteBuffer.allocate(REQUEST_HEAD_SIZE + packetToByte.length);
		buffer.put(message.getMessageType().toByte());
		buffer.putInt(packetToByte.length);
		buffer.put(packetToByte);

		return buffer.array();
	}

	public static RequestMessage byteToRequest(byte[] data) {
		ByteBuffer wrap = ByteBuffer.wrap(data);
		RequestMessage message = new RequestMessage();
		message.setMessageType(MessageType.fromByte(wrap.get()));
		int length = wrap.getInt();
		message.setLength(length);
		// byte[] bytes = new byte[length];
		// wrap.get(bytes);
		// Packet byteToPacket = byteToPacket(bytes);
		// message.setPacket(byteToPacket);
		return message;
	}

	public static byte[] packetToByte(Packet packet) {
		String data = packet.getData();
		byte[] bytes = data.getBytes();

		ByteBuffer buffer = ByteBuffer.allocate(PACKET_HEAD_SIZE + bytes.length);
		buffer.put(packet.getMark());
		buffer.putInt(packet.getChecksum());
		
		buffer.putInt(bytes.length);
		buffer.put(bytes);
		return buffer.array();
	}

	public static Packet byteToPacket(byte[] data) {
		ByteBuffer wrap = ByteBuffer.wrap(data);
		byte mark = wrap.get();
		int checksum = wrap.getInt();
		
		int length = wrap.getInt();
		byte[] bytes = new byte[length];
		wrap.get(bytes);
		Packet packet = new Packet(new String(bytes),checksum,mark);
		return packet;
	}

	public static ResponseMessage byteToReponse(byte[] data) {
		ByteBuffer wrap = ByteBuffer.wrap(data);
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setMessageType(MessageType.fromByte(wrap.get()));
		int length = wrap.getInt();
		responseMessage.setLength(length);
		return responseMessage;
	}

	public static byte[] responseToByte(ResponseMessage responseMessage) {
		Packet packet = responseMessage.getPacket();
		byte[] packetToByte = packetToByte(packet);

		ByteBuffer buffer = ByteBuffer.allocate(RESPONSE_HEAD_SIZE + packetToByte.length);
		buffer.put(responseMessage.getMessageType().toByte());
		buffer.putInt(packetToByte.length);
		buffer.put(packetToByte);

		return buffer.array();
	}
}
