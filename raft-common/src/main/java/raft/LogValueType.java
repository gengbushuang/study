package raft;

public enum LogValueType {

    Application{
        @Override
        public byte toByte() {
            return (byte) 1;
        }
    },
    Configuration {
        @Override
        public byte toByte(){
            return(byte) 2;
        }
    }
    ;

    public abstract byte toByte();

    public static LogValueType fromByte(byte b){
        switch(b) {
            case 1:
                return Application;
            case 2:
                return Configuration;
            default:
                throw new IllegalArgumentException(String.format("%d is not defined for LogValueType", b));
        }
    }
}