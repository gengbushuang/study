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
}
