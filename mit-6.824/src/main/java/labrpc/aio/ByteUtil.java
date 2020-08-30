package labrpc.aio;

public class ByteUtil {

    public static int intLen = Integer.BYTES;

    public static int getLowInt(byte[] bytes) {
        return makeInt(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    public static int getHagInt(byte[] bytes) {
        return makeInt(bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) << 8) |
                ((b0 & 0xff)));
    }
}
