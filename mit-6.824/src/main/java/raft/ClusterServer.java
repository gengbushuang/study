package raft;

public class ClusterServer {
    private int id;
    private String endpoint;


    public ClusterServer(int id, String endpoint) {
        this.id = id;
        this.endpoint = endpoint;
    }

    public int getId() {
        return id;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
