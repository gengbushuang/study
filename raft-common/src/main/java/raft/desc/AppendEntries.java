package raft.desc;

/**
 * 由Leader进行调用，用于将日志记录备份至Follower，同时还会被用来作为心跳信息
 */
public class AppendEntries {

    class AppendEntriesRequest{
        //Leader的Term ID
        private long term;
        //Leader的ID
        private int leaderId;
        //正在备份的日志之前的日志记录的index值
        private long prevLogIndex;
        //正在备份的日志记录之前的日志记录的Term ID
        private long prevLogTerm;
        //

        //Leader已经提交的最好一条日志记录的index值
        private long commitIndex;
    }

    class AppendEntriesResponse{
        //接收方的当前Term ID
        private long term;
        //当Follower能够在自己的日志中找到index值和Term ID与prevLogIndex和prevLogTerm相同的记录时为true
        private boolean success;
    }

    /**
     * 接收方在接收到该RPC后进行一下操作
     * 1.若term < currentTerm,返回false
     * 2.若日志中不包含index值和Term ID与prevLogIndex和prevLogTerm相同的记录，返回false
     * 3.如果日志中存在与正在备份的日志记录相冲突的记录(有相同的index值但Term ID不同)，删除该记录以及之后的所有记录
     * 4.在保存的日志后追加新的日志记录
     * 5.若leaderCommit > commitIndex,令commitIndex等于leaderCommit和最后一个新日志记录的index值之间的最小值
     */

}
