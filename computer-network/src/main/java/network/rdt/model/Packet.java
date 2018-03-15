package network.rdt.model;

public class Packet {

	private final String data;
	private final int checksum;
	private final byte mark;
	
	public Packet(String _data, int _checksum, byte _mark){
		this.data = _data;
		this.checksum = _checksum;
		this.mark = _mark;
	}

	public String getData() {
		return data;
	}

	public int getChecksum() {
		return checksum;
	}

	public byte getMark() {
		return mark;
	}

	@Override
	public String toString() {
		return "Packet [data=" + data + ", checksum=" + checksum + ", mark=" + mark + "]";
	}

}
