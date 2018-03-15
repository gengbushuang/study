package network.message;

import network.rdt.model.Packet;


public class RequestMessage {

	private MessageType messageType;

	private int length;

	private Packet packet;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "RequestMessage [messageType=" + messageType + ", length=" + length + ", packet=" + packet + "]";
	}

}
