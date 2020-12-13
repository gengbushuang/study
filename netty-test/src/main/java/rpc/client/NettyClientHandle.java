package rpc.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageCodec;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;
import rpc.serialize.SerializationUtil;
import rpc.utils.WriteQueue;

import java.util.List;

public class NettyClientHandle extends ByteToMessageCodec<RpcRequest> {

    private WriteQueue clientWriteQueue;

    private Class<?> encode;

    private Class<?> decode;

    private NettyClientHandle(Class<?> encode, Class<?> decode) {
        this.encode = encode;
        this.decode = decode;
    }

    static NettyClientHandle newHandle() {
        return new NettyClientHandle(RpcRequest.class, RpcResponse.class);
    }


    void startWriteQueue(Channel channel) {
        clientWriteQueue = new WriteQueue(channel);
    }

    WriteQueue getWriteQueue() {
        return clientWriteQueue;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) throws Exception {
//        System.out.println("NettyClientHandle-----------encode");
        byte[] data = SerializationUtil.serialize(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
//        System.out.println("NettyClientHandle-----------decode");
        int i = byteBuf.readableBytes();
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        //标记当前下标
        byteBuf.markReaderIndex();
        //获取数据长度
        int dataLength = byteBuf.readInt();
        //长度小于0表示有异常
        if (dataLength < 0) {
            ctx.close();
        }
        i = byteBuf.readableBytes();
        //判断剩余的字节长度是否大于数据长度
        if (byteBuf.readableBytes() < dataLength) {
            //如果剩余长度不够，还原到标记下标位置
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        //反序列化
        Object o = SerializationUtil.deserialize(data, this.decode);
        out.add(o);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandle--------------handlerAdded");
//        super.handlerAdded(ctx);
        ctx.pipeline().addLast(new RpcClientHandler());
    }

    private class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
//            System.out.println("-->"+response);
            clientWriteQueue.remove(response);
        }
    }

}
