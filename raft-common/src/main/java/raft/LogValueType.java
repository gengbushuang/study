package raft;

public enum LogValueType {

    Application{
        @Override
        public byte toByte() {
            return (byte) 1;
        }
    }
    ;

    public abstract byte toByte();
}