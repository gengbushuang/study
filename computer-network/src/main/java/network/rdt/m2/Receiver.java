package network.rdt.m2;

import network.rdt.model.Packet;

public class Receiver {

	private final static int NAK = 0;
	private final static int ACK = 1;

	// 检验码，先有java自带的hash代替
	public int check_sum(String data) {
		return data.hashCode();
	}
	
	public void rtd_rcv(Packet packet) {
		
	}

	public void udt_send(Packet sndpkt) {
		System.out.println("服务器端发送:");
		show(sndpkt);
	}

	public String extract(Packet packet) {
		return null;
	}

	public void deliver_data(String data) {

	}

	public Packet make_pkt(String data,int mark) {
		return new Packet(data, 0, (byte) mark);
	}
	
	public Packet make_pkt_ack(){
		return make_pkt("ACK",ACK);
	}
	
	public Packet make_pkt_nak(){
		return make_pkt("NAK",NAK);
	}

	// rdt2.0接收方只有一个状态
	public Packet rdt_rcv(Packet rcvpkt) {
		System.out.println("服务器端接收:");
		show(rcvpkt);
		Packet sndpkt;
		if (rcvpkt != null && notcorrupt(rcvpkt)) {
			// extract(packet,data)
			String data = extract(rcvpkt);
			deliver_data(data);
			sndpkt = make_pkt("ACK",ACK);
		} else {
			sndpkt = make_pkt("NAK",NAK);
		}
		return sndpkt;
		//udt_send(sndpkt);
	}

	public boolean corrupt(Packet rcvpkt) {
		return rcvpkt.getChecksum() != check_sum(rcvpkt.getData());
	}

	public boolean notcorrupt(Packet rcvpkt) {
		return rcvpkt.getChecksum() == check_sum(rcvpkt.getData());
	}

	public void show(Packet rcvpkt) {
		System.out.println(rcvpkt);
	}
}
