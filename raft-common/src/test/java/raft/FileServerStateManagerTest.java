package raft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import raft.file.FileServerStateManager;

/**
 * Created by gbs on 19/3/27.
 */
public class FileServerStateManagerTest {

    String path = "/Users/gbs/tmp/tmp_raft/server1";

    FileServerStateManager fileServerStateManager;

    @Before
    public void before(){
        fileServerStateManager = new FileServerStateManager(path);

    }
    @After
    public void after(){
        System.out.println("after");
        fileServerStateManager.close();
        fileServerStateManager = null;
    }


    @Test
    public void testLoadClusterConfiguration(){
        ClusterConfiguration clusterConfiguration = fileServerStateManager.loadClusterConfiguration();
    }

}
