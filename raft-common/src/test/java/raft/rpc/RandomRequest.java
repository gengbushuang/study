package raft.rpc;

import raft.RaftMessageType;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by gbs on 19/3/27.
 */
public class RandomRequest {
    private Random random = new Random(Calendar.getInstance().getTimeInMillis());

    private RaftMessageType randomMessageType(){
        byte value = (byte)this.random.nextInt(5);
        return RaftMessageType.fromByte((byte) (value + 1));
    }

}
