package raft.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.ClusterConfiguration;
import raft.ServerState;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.InputStreamReader;

/**
 * Created by gbs on 19/3/27.
 */
public class FileServerStateManager {
    private static final String STATE_FILE = "server.state";
    private static final String CONFIG_FILE = "config.properties";
    private static final String CLUSTER_CONFIIG_FILE = "cluster.json";

    private RandomAccessFile serverStateFile;
    private FileLogStore logStore;
    private Path container;

    private Logger logger;

    private int serverId;

    public FileServerStateManager(String dataDirectory) {
        this.logger = LogManager.getLogger(getClass());
        this.logStore = new FileLogStore(dataDirectory, 1000);
        this.container = Paths.get(dataDirectory);
        try {
            Properties props = new Properties();
            FileInputStream configInput = new FileInputStream(this.container.resolve(CONFIG_FILE).toString());
            props.load(configInput);
            String serverIdValue = props.getProperty("server.id");
            this.serverId = serverIdValue == null || serverIdValue.length() == 0 ? -1 : Integer.parseInt(serverIdValue.trim());
            configInput.close();

            this.serverStateFile = new RandomAccessFile(this.container.resolve(STATE_FILE).toString(), "rw");
            this.serverStateFile.seek(0);

        } catch (IOException e) {
            this.logger.error("failed to create/open server state file", e);
            throw new IllegalArgumentException("cannot create/open the state file", e);
        }
    }

    public ClusterConfiguration loadClusterConfiguration() {
        Gson gson = new GsonBuilder().create();
        try (FileInputStream stream = new FileInputStream(this.container.resolve(CLUSTER_CONFIIG_FILE).toString())) {
            ClusterConfiguration config = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), ClusterConfiguration.class);
            return config;
        } catch (IOException e) {
            this.logger.error("failed to read cluster configuration", e);
            throw new RuntimeException("failed to read in cluster config", e);
        }
    }

    public void saveClusterConfiguration(ClusterConfiguration configuration) {
        Gson gson = new GsonBuilder().create();
        String configData = gson.toJson(configuration);
        try {
            Files.deleteIfExists(this.container.resolve(CLUSTER_CONFIIG_FILE));
            FileOutputStream output = new FileOutputStream(this.container.resolve(CLUSTER_CONFIIG_FILE).toString());
            output.write(configData.getBytes(StandardCharsets.UTF_16));
            output.flush();
            output.close();
        } catch (IOException e) {
            this.logger.error("failed to save cluster config to file", e);
        }
    }

    public synchronized void persistState(ServerState serverState) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2 + Integer.BYTES);
            buffer.putLong(serverState.getTerm());
            buffer.putLong(serverState.getCommitIndex());
            buffer.putInt(serverState.getVotedFor());
            this.serverStateFile.write(buffer.array());
            this.serverStateFile.seek(0);
        } catch (IOException e) {
            this.logger.error("failed to write to the server state file", e);
            throw new RuntimeException("fatal I/O error while writing to the state file", e);
        }
    }

    public synchronized ServerState readState() {
        try {
            if (this.serverStateFile.length() == 0) {
                return null;
            }
            byte[] stateData = new byte[Long.BYTES * 2 + Integer.BYTES];
            this.read(stateData);
            this.serverStateFile.seek(0);
            ByteBuffer buffer = ByteBuffer.wrap(stateData);
            ServerState serverState = new ServerState();
            serverState.setTerm(buffer.getLong());
            serverState.setCommitIndex(buffer.getLong());
            serverState.setVotedFor(buffer.getInt());
            return serverState;
        } catch (IOException e) {
            this.logger.error("failed to read from the server state file", e);
            throw new RuntimeException("fatal I/O error while reading from state file", e);
        }
    }

    private void read(byte[] buffer) {
        try{
            int offset = 0;
            int bytesRead = 0;
            while(offset < buffer.length && (bytesRead = this.serverStateFile.read(buffer, offset, buffer.length - offset)) != -1){
                offset += bytesRead;
            }

            if(offset < buffer.length){
                this.logger.error(String.format("only %d bytes are read while %d bytes are desired, bad file", offset, buffer.length));
                throw new RuntimeException("bad file, insufficient file data for reading");
            }
        }catch(IOException exception){
            this.logger.error("failed to read and fill the buffer", exception);
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }


    public FileLogStore loadLogStore(){
        return this.logStore;
    }

    public int getServerId() {
        return this.serverId;
    }

    public void close() {
        try {
            this.serverStateFile.close();
            this.logStore.close();
        } catch (IOException e) {
            this.logger.info("failed to shutdown the server state manager due to io error", e);
        }
    }
}
