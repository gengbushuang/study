package raft;

public enum RaftMessageType {

    /**
     * RequestVote RPC
     */

    RequestVoteRequest{
        @Override
        public byte toByte() {
            return (byte)1;
        }
    },
    RequestVoteResponse{
        @Override
        public byte toByte() {
            return(byte)2;
        }
    },
    /**
     * AppendEntries RPC
     */
    AppendEntriesRequest{
        @Override
        public byte toByte() {
            return (byte)3;
        }
    },
    AppendEntriesResponse{
        @Override
        public byte toByte() {
            return (byte)4;
        }
    }
    ;

    public abstract byte toByte();

    public static RaftMessageType fromByte(byte value) {
        switch (value){
            case 1:
                return RequestVoteRequest;
            case 2:
                return RequestVoteResponse;
            case 3:
                return AppendEntriesRequest;
            case 4:
                return AppendEntriesResponse;

        }
        throw new IllegalArgumentException("the value for the message type is not defined");
    }
}
