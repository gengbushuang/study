package labrpc.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AioClientTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AioClient client = new AioClient(new InetSocketAddress("127.0.0.1", 8989), Executors.newFixedThreadPool(4));
        String tmp = "";
        for (int i = 0; i < 10; i++) {
            tmp = "";
            for (int j = i; j >= 0; j--) {
                tmp += "a";
            }
            System.out.println("req -->" + tmp);
            byte[] bytes = tmp.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + 4);
            byteBuffer.putInt(bytes.length);
            byteBuffer.put(bytes);
            byte[] array = byteBuffer.array();
            CompletableFuture<byte[]> future = client.send(array);
            byte[] rep = future.get();
            System.out.println("repy -->" + new String(rep));
        }

    }
}
