package rpc.test;

public class HelloWordImpl implements HelloWord {
    @Override
    public String sysHello(String name) {
        return name+" hello word";
    }
}
