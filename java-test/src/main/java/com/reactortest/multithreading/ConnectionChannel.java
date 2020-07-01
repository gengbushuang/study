package com.reactortest.multithreading;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ConnectionChannel {

    private final SocketChannel channel;

    volatile SelectionKey selectionKey;

    protected final int readInterestOp;

    private volatile SubReactor subReactor;

    public ConnectionChannel(SocketChannel channel){
        this.channel = channel;
        this.readInterestOp = SelectionKey.OP_READ;
    }

    public void register(SubReactor subReactor) throws ClosedChannelException {
        ConnectionChannel.this.subReactor = subReactor;


        if(channel.isOpen() && channel.isConnected()){

        }

        doRegister();

        channel.register(subReactor.getSelector(),readInterestOp,this);
    }

    public void doRegister(){
        for (;;) {
            try {
                this.selectionKey = channel.register(subReactor.getSelector(),0,this);
                return;
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }
    }

}
