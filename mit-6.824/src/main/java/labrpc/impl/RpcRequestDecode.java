package labrpc.impl;

import labrpc.ByteDecode;
import labrpc.aio.ByteUtil;
import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;
import labrpc.serialize.SerializationUtil;

import java.nio.ByteBuffer;

public class RpcRequestDecode extends ByteDecode<RpcRequest> {

    @Override
    protected byte[] decodeToByte(RpcRequest rpcRequest) {
        byte[] bytes = SerializationUtil.serialize(rpcRequest);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + ByteUtil.intLen);
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);
        return byteBuffer.array();
    }
}
