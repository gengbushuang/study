package network.message;

import network.rdt.model.Packet;

public class ResponseMessage {

	private MessageType messageType;

	private int length;

	private Packet packet;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	@Override
	public String toString() {
		return "ResponseMessage [messageType=" + messageType + ", length=" + length + ", packet=" + packet + "]";
	}

}
