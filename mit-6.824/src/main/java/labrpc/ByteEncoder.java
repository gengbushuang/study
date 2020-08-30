package labrpc;

public abstract class ByteEncoder<OUT> implements Encoder<byte[], OUT> {
    @Override
    public OUT encoder(byte[] bytes) {
        return ByteEncoder(bytes);
    }

    protected abstract OUT ByteEncoder(byte[] bytes);
}
