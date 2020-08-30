package raft.t;

abstract class AbstractNodeRole {

    private final ServerRole name;

    protected final int trem;

    AbstractNodeRole(ServerRole name,int term){
        this.name = name;
        this.trem = term;
    }

    public ServerRole getName(){
        return name;
    }

    public abstract void cancelTimeoutOrTask();

    public int getTrem(){
        return trem;
    }
}
