package labrpc.aio;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

public class AioServiceTest {
    public static void main(String[] args) throws InterruptedException {
        AioServer server = new AioServer(8989, Executors.newSingleThreadExecutor());

        server.startListening(new Dispatcher() {
            @Override
            public byte[] processByte(byte[] data) {
                System.out.println(new String(data));
                byte[] bytes = "success".getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 4);
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                return buffer.array();
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
    }
}
