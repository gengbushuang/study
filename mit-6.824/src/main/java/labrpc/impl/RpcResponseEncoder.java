package labrpc.impl;

import labrpc.ByteEncoder;
import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;
import labrpc.serialize.SerializationUtil;

public class RpcResponseEncoder extends ByteEncoder<RpcResponse> {
    @Override
    protected RpcResponse ByteEncoder(byte[] bytes) {
        return SerializationUtil.deserialize(bytes, RpcResponse.class);
    }
}
