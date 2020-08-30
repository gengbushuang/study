package labrpc.impl;

import labrpc.ByteEncoder;
import labrpc.mobel.RpcRequest;
import labrpc.serialize.SerializationUtil;

public class RpcRequestEncoder extends ByteEncoder<RpcRequest> {
    @Override
    protected RpcRequest ByteEncoder(byte[] bytes) {
        return SerializationUtil.deserialize(bytes, RpcRequest.class);
    }
}
