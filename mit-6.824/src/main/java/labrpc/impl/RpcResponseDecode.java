package labrpc.impl;

import labrpc.ByteDecode;
import labrpc.aio.ByteUtil;
import labrpc.mobel.RpcResponse;
import labrpc.serialize.SerializationUtil;

import java.nio.ByteBuffer;

public class RpcResponseDecode extends ByteDecode<RpcResponse> {

    @Override
    protected byte[] decodeToByte(RpcResponse rpcResponse) {
        byte[] bytes = SerializationUtil.serialize(rpcResponse);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + ByteUtil.intLen);
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);
        return byteBuffer.array();
    }
}
