package raft.file;

import raft.ClusterConfiguration;
import raft.Snapshot;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by gbs on 19/3/26.
 */
public class FileSnapshotState {

    private Path snapshotStore;
    private long commitIndex;

    private int port;

    public FileSnapshotState(Path baseDir, int listeningPort){
        this.port = listeningPort;
        this.snapshotStore = baseDir.resolve("snapshots");
        this.commitIndex = 0;
        if(!Files.isDirectory(this.snapshotStore)){
            try {
                Files.createDirectory(this.snapshotStore);
            }catch (Exception error){
                throw new IllegalArgumentException("bad baseDir");
            }
        }
    }

    public void saveSnapshotData(Snapshot snapshot,long offset,byte[]data){
        //下标和任期号拼接文件名
        Path filePath = this.snapshotStore.resolve(String.format("%d-%d.s",snapshot.getLastLogIndex(),snapshot.getLastLogTerm()));
        try {
            if (!Files.exists(filePath)) {
                //
                Files.write(this.snapshotStore.resolve(String.format("%d.cnf", snapshot.getLastLogIndex())), snapshot.getLastConfig().toBytes(), StandardOpenOption.CREATE);
            }

            RandomAccessFile snapshotFile = new RandomAccessFile(filePath.toString(),"rw");
            snapshotFile.seek(offset);
            snapshotFile.write(data);
            snapshotFile.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Snapshot getLastSnapshot(){
        try {
            //获取这个目录下所有文件
            Stream<Path> files = Files.list(this.snapshotStore);
            Path lasestSnapshot = null;
            long maxLastLogIndex = 0;
            long term = 0;
            Pattern pattern = Pattern.compile("(\\d+)\\-(\\d+)\\.s");
            Iterator<Path> itor = files.iterator();
            while (itor.hasNext()){
                Path file = itor.next();
                //判断这个是不是正常文件
                if(Files.isRegularFile(file)){
                    Matcher matcher = pattern.matcher(file.getFileName().toString());
                    if(matcher.matches()){
                        long lastLogIndex = Long.parseLong(matcher.group(1));
                        if(lastLogIndex > maxLastLogIndex){
                            maxLastLogIndex = lastLogIndex;
                            term = Long.parseLong(matcher.group(2));
                            lasestSnapshot = file;
                        }
                    }
                }
            }
            files.close();
            if(lasestSnapshot!=null){
                byte[] configData = Files.readAllBytes(this.snapshotStore.resolve(String.format("%d.cnf", maxLastLogIndex)));
                ClusterConfiguration config = ClusterConfiguration.fromBytes(configData);
                return new Snapshot(maxLastLogIndex,term,config,lasestSnapshot.toFile().length());
            }
        }catch (Exception e){

        }
        return  null;
    }
}
