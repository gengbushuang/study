package raft;

import java.util.LinkedList;
import java.util.List;

public class ClusterConfiguration {

    private List<ClusterServer> servers;

    public ClusterConfiguration() {
        this.servers = new LinkedList<>();
    }

    public void addServers(ClusterServer clusterServer){
        servers.add(clusterServer);
    }

    public void setServers(List<ClusterServer> servers) {
        this.servers = servers;
    }

    public List<ClusterServer> getServers() {
        return servers;
    }

    public ClusterServer getServer(int id) {
        for (ClusterServer server : this.servers) {
            if (server.getId() == id) {
                return server;
            }
        }
        return null;
    }
}
