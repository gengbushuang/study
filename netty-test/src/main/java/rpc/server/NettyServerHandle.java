package rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageCodec;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;
import rpc.serialize.SerializationUtil;

import java.lang.reflect.Method;
import java.util.List;

public class NettyServerHandle extends ByteToMessageCodec<RpcResponse> {

    InterfaceRegistry registry = InterfaceRegistry.getInstance();

    private Class<?> decode = RpcRequest.class;

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) throws Exception {
        byte[] data = SerializationUtil.serialize(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
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
        System.out.println("NettyServerHandle--------------handlerAdded");
        ctx.pipeline().addLast(new RpcServerHandle());
    }

    private class RpcServerHandle extends SimpleChannelInboundHandler<RpcRequest> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try {
                Object result = processRequest(request);
                response.setResult(result);
            } catch (Throwable e) {
                System.out.println(request.getRequestId() + "--" + e);
                response.setError(e);
            }
            ctx.writeAndFlush(response);
        }

        private Object processRequest(RpcRequest request) throws Throwable {
            Thread.sleep(3000);
            System.out.println(request.getRequestId() + "--" + request.getClassName());
            String className = request.getClassName();
            Object object = registry.findObject(request);
            //方法名
            String methodName = request.getMethodName();
            //方法的参数类型
            Class<?>[] parameterClasses = request.getParameterClasses();
            //方法参数
            Object[] parameters = request.getParameters();
            //获取方法对象
            Method method = object.getClass().getMethod(methodName, parameterClasses);
            //调用类对应的方法
            Object invoke = method.invoke(object, parameters);

            return invoke;
        }
    }
}
