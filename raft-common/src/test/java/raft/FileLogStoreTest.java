package raft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import raft.file.FileLogStore;

/**
 * Created by gbs on 19/3/26.
 */
public class FileLogStoreTest {

    String path = "/Users/gbs/tmp/tmp_raft/store";

    FileLogStore fileLogStore;

    @Before
    public void before(){
        fileLogStore = new FileLogStore(path,10);
    }
    @After
    public void after(){
        System.out.println("after");
        fileLogStore.close();
        fileLogStore = null;
    }

    @Test
    public void testInit(){

    }

    @Test
    public void testAppend(){
        for(int i = 5;i<15;i++) {
            int trem = i;
            byte[] b = ("你说什么啊"+i).getBytes();
            LogEntry logEntry = new LogEntry(trem, b);
            fileLogStore.append(logEntry);
        }
    }

    @Test
    public void testGetLogEntryAt(){
        long logIndex = 3;
        LogEntry logEntryAt = fileLogStore.getLogEntryAt(logIndex);

    }

    @Test
    public void testGetFirstAvailableIndex(){
        long logLastIndex = fileLogStore.getFirstAvailableIndex();
        System.out.println("FirstAvailableIndex->"+logLastIndex);
    }

    @Test
    public void testGetLogEntries(){
        long startLog = 7;
        long startEnd = 9;
        LogEntry[] logEntries = fileLogStore.getLogEntries(startLog, startEnd);
    }
}
