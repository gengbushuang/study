package raft.t;

import java.util.concurrent.ScheduledFuture;

public class ElectionTimeout {
    private final ScheduledFuture<?> scheduledFuture;

    public ElectionTimeout(ScheduledFuture<?> scheduledFuture){
        this.scheduledFuture =scheduledFuture;
    }

    public void cancel(){
        this.scheduledFuture.cancel(false);
    }
}
