package com.reactortest.singlethread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {

    final SelectionKey selectionKey;
    final SocketChannel socketChannel;

    static final int READING = 0, SENDING = 1;

    int state;

    ByteBuffer input = ByteBuffer.allocate(50);
    ByteBuffer output = ByteBuffer.allocate(50);

    int count;

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        this.state = READING;
        this.selectionKey = this.socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        System.out.println(count++);
        try {
            if (state == READING) {
                int length = 0;
                while ((length = socketChannel.read(input)) > 0) {
                    String s = new String(input.array(), 0, length);
                    System.out.println(s);
                }
                System.out.println("读取一批数据完成");
                input.clear();
                state = SENDING;

                //selectionKey.attach(this);
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                //selectionKey.selector().wakeup();
            } else if (state == SENDING) {
                output.clear();
                output.put(ByteBuffer.wrap("好了\n".getBytes()));
                output.flip();
                socketChannel.write(output);
                System.out.println("写入一批数据完成");
                state = READING;

                //selectionKey.attach(this);
                selectionKey.interestOps(SelectionKey.OP_READ);
                //selectionKey.selector().wakeup();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
