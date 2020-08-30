package raft.t;

public class NodeEndpoint {
    private final int id;
    private final Address address;

    public NodeEndpoint(int id, String host, int port) {
        this(id, new Address(host, port));
    }

    public NodeEndpoint(int id, Address address) {
        this.id = id;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }
}
