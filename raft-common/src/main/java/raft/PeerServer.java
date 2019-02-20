package raft;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

public class PeerServer {

    private boolean heartbeatEnabled;

    private int currentHeartbeatInterval;
    private int heartbeatInterval;

    private ScheduledFuture<?> heartbeatTask;

    /**心跳任务*/
    private Callable<Void> heartbeatTimeoutHandler;

    public PeerServer(){

        this.heartbeatTask = null;
    }

    public synchronized int getCurrentHeartbeatInterval(){
        return this.currentHeartbeatInterval;
    }


    public Callable<Void> getHeartbeartHandler(){
        return this.heartbeatTimeoutHandler;
    }

    public void enableHeartbeat(boolean enable) {
        this.heartbeatEnabled = enable;
        if(!enable){
            this.heartbeatTask = null;
        }
    }

    public synchronized void resumeHeartbeatingSpeed() {
        //如果当前心跳时间小于设置的心跳时间
        //重新赋值当前的心跳时间
        if (this.currentHeartbeatInterval > this.heartbeatInterval) {
            this.currentHeartbeatInterval = this.heartbeatInterval;
        }
    }

    public void setHeartbeatTask(ScheduledFuture<?> heartbeatTask) {
        this.heartbeatTask = heartbeatTask;
    }
}
