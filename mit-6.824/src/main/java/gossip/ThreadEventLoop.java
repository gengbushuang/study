package gossip;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ThreadEventLoop {


    private Thread thread;

    DatagramSocket socket;

    public ThreadEventLoop() {


    }

    public void init() {
        try {
            socket = new DatagramSocket(8800);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        thread = new Thread();
        thread.start();
    }


}
