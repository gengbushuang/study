package com.reactortest.multithreading;

import com.reactortest.singlethread.Reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public class MainReactor implements Runnable {

    private final SubReactor[] reactors;

    private final SubReactorChooserFactory.SubReactorChooser subReactorChooser;

    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    public MainReactor(int port, int thread) throws IOException {
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT, new Acceptor());

        Executor executor = new DefaultThreadExecute(new DefaultThreadFactory(getClass().getName()));

        this.reactors = new SubReactor[thread];
        for (int i = 0; i < thread; i++) {
            this.reactors[i] = new SubReactor(Selector.open(), executor);
        }

        subReactorChooser = DefaultSubReactorChooserFactory.INSTANCE.newChooser(this.reactors);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    dispatch(key);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }


    class Acceptor implements Runnable {
        private final boolean tcpNoDelay = false;
        @Override
        public void run() {
            try {
                System.out.println("Acceptor:run-->" + Thread.currentThread());
                SocketChannel accept = serverSocketChannel.accept();
                if (accept != null) {
                    ConnectionChannel connectionChannel = new ConnectionChannel(accept);
                    connectionChannel.register(subReactorChooser.next());
//                    accept.configureBlocking(false);
//                    accept.socket().setTcpNoDelay(tcpNoDelay);
//                    accept.socket().setKeepAlive(true);
//                    subReactorChooser.next().registerChannl(accept);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MainReactor mainReactor = new MainReactor(8089, 4);
        mainReactor.run();
    }
}
