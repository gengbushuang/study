package network.message;

public enum MessageType {

	RequestRPC {
		@Override
		public byte toByte() {
			return 1;
		}

	},

	ResponseRPC {

		@Override
		public byte toByte() {
			return 2;
		}

	},
	RequestRdt {

		@Override
		public byte toByte() {
			return 3;
		}

	},
	ResponseRdt {

		@Override
		public byte toByte() {
			return 4;
		}

	};

	public abstract byte toByte();

	public static MessageType fromByte(byte value) {
		switch (value) {
		case 1:
			return RequestRPC;
		case 2:
			return ResponseRPC;
		case 3:
			return RequestRdt;
		case 4:
			return ResponseRdt;
		}
		throw new IllegalArgumentException("the value for the message type is not defined");
	}
}
