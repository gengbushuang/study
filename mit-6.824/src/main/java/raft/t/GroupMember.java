package raft.t;

public class GroupMember {
    private final NodeEndpoint endpoint;

    GroupMember(NodeEndpoint endpoint) {
        this(endpoint, null);
    }

    GroupMember(NodeEndpoint endpoint, Integer integer) {
        this.endpoint = endpoint;
    }

    public NodeEndpoint getEndpoint() {
        return endpoint;
    }

    public boolean idEquals(int selfId) {
        return endpoint.getId() == selfId;
    }
}
