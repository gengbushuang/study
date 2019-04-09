package raft;

import raft.file.FileServerStateManager;

/**
 * Created by gbs on 19/3/28.
 */
public class RaftContext {
    private FileServerStateManager serverStateManager;

    private RaftOptions raftOptions;
}
