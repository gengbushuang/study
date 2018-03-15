package network.message.impl;

import network.message.MessageHandler;
import network.message.MessageType;
import network.message.RequestMessage;
import network.message.ResponseMessage;
import network.rdt.m2.Receiver;
import network.rdt.model.Packet;

public class MessageHandlerImpl implements MessageHandler {

	@Override
	public ResponseMessage processRequest(RequestMessage requestMessage) {
		if(requestMessage.getMessageType()==MessageType.RequestRdt){
			return handlerRequestRdt(requestMessage);
		}
		return null;
	}

	private ResponseMessage handlerRequestRdt(RequestMessage requestMessage) {
		System.out.println(requestMessage);
		Packet packet = requestMessage.getPacket();
		Packet sndpkt = rtd_rcv(packet);
		
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessageType(MessageType.ResponseRdt);
		
		responseMessage.setPacket(sndpkt);
		return responseMessage;
	}

	Receiver receiver = new Receiver();
	private Packet rtd_rcv(Packet rcvpkt) {
		System.out.println("服务器端接收:");
		Packet sndpkt;
		if (rcvpkt != null && receiver.notcorrupt(rcvpkt)) {
			String data = receiver.extract(rcvpkt);
			receiver.deliver_data(data);
			sndpkt = receiver.make_pkt_ack();
		} else {
			sndpkt = receiver.make_pkt_nak();
		}
		return sndpkt;
	}

}
