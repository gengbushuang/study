package labrpc;

public abstract class ByteDecode<IN> implements Decoder<IN, byte[]> {

    @Override
    public byte[] decode(IN in) {
        return decodeToByte(in);
    }

    protected abstract byte[] decodeToByte(IN in);
}
