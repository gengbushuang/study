package raft;

import raft.message.Entry;

import java.util.List;

public interface SequenceLogStore extends AutoCloseable {


    /**
     * 获取下一条日志索引
     * @return
     */
    public long getNextLogIndex();
    /**
     * 追加日志条目
     *
     * @param entry
     * @return
     */
    public long append(Entry entry);

    /**
     * 获取某条日志
     *
     * @param index
     * @return
     */
    public Entry getEntry(Integer index);

    /**
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public Entry[] getListEntrys(Integer fromIndex, Integer toIndex);

    /**
     * 获取最后条日志
     *
     * @return
     */
    public Entry getLastEntry();

}
