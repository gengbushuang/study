package raft.t;

import java.util.concurrent.ScheduledFuture;

public class LogReplicationTask {

    private final ScheduledFuture<?> scheduledFuture;

    public LogReplicationTask(ScheduledFuture<?> scheduledFuture){
        this.scheduledFuture = scheduledFuture;
    }

    public void cancel(){
        this.scheduledFuture.cancel(false);
    }
}
