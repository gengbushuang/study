package raft;

import raft.impl.FileLogStore;
import raft.message.Entry;
import raft.message.request.GeneralEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RaftFileTest {


    public void testAppandFile() throws Exception {
        String path = "logstore";
        SequenceLogStore logStore = new FileLogStore(path);
        for (int i = 0; i < 1000; i++) {
            Entry entry = new GeneralEntry(1, i, 2, "xxghhsdfxxx".getBytes());
            logStore.append(entry);
        }

        logStore.close();
    }

    public void testReadFile() throws Exception {
        String path = "logstore";
        SequenceLogStore logStore = new FileLogStore(path);

        Entry entry = logStore.getEntry(767);
        System.out.println(entry);
        logStore.close();
    }

    public void testReadsFile() throws Exception {
        String path = "logstore";
        SequenceLogStore logStore = new FileLogStore(path);
//        Entry[] listEntrys = logStore.getListEntrys(100, 512);
//        for (Entry e : listEntrys) {
//            System.out.println(e);
//        }
        logStore.close();

    }
    
    public void testLastFile() throws Exception {
        String path = "logstore";
        SequenceLogStore logStore = new FileLogStore(path);
        Entry lastEntry = logStore.getLastEntry();
        System.out.println(lastEntry);
        logStore.close();
    }


    public static void main(String[] args) throws Exception {
        RaftFileTest raftFileTest = new RaftFileTest();
//        raftFileTest.testAppandFile();
//        raftFileTest.testReadFile();
//        raftFileTest.testReadsFile();
        raftFileTest.testLastFile();


    }
}
