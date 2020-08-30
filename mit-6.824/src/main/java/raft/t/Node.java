package raft.t;

public interface Node {

    void start();

    void stop() throws InterruptedException;
}
