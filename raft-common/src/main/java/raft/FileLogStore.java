package raft;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileLogStore {
    private final static String LOG_INDEX_FILE = "store.idx";
    private final static String LOG_STORE_FILE = "store.data";
    private final static String LOG_START_INDEX_FILE = "store.sti";

    private static final LogEntry zeroEntry = new LogEntry();


    private RandomAccessFile indexFile;
    private RandomAccessFile dataFile;
    private RandomAccessFile startIndexFile;
    //开始索引位置
    private long startIndex;
    private long entriesInStore;


    //读写锁
    private ReentrantReadWriteLock storeLock;
    //读锁
    private ReentrantReadWriteLock.ReadLock storeReadLock;
    //写锁
    private ReentrantReadWriteLock.WriteLock storeWriteLock;
    //目录
    private Path logContainer;
    //日志缓存大小
    private int bufferSize;

    private LogBuffer buffer;

    public FileLogStore(String logContainer, int bufferSize) {
        this.storeLock = new ReentrantReadWriteLock();
        this.storeReadLock = this.storeLock.readLock();
        this.storeWriteLock = this.storeLock.writeLock();
        this.logContainer = Paths.get(logContainer);
        this.bufferSize = bufferSize;


        try {
            this.indexFile = new RandomAccessFile(this.logContainer.resolve(LOG_INDEX_FILE).toString(), "rw");
            this.dataFile = new RandomAccessFile(this.logContainer.resolve(LOG_STORE_FILE).toString(), "rw");
            this.startIndexFile = new RandomAccessFile(this.logContainer.resolve(LOG_START_INDEX_FILE).toString(), "rw");
            //查看文件里面是否有数据
            if (this.startIndexFile.length() == 0) {
                this.startIndex = 1;
                this.startIndexFile.writeLong(this.startIndex);
            } else {
                this.startIndex = this.startIndexFile.readLong();
            }
            //日志记录
            this.entriesInStore = this.indexFile.length() / Long.BYTES;
            this.buffer = new LogBuffer(this.entriesInStore > this.bufferSize ? (this.entriesInStore + this.startIndex - this.bufferSize) : this.startIndex, this.bufferSize);
            this.fillBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fillBuffer() throws IOException {
        //获取
        long startIndex = this.buffer.firstIndex();
        long indexFileSize = this.indexFile.length();
        if (indexFileSize > 0) {

        }
    }

    /**
     * 获取可用的索引下标
     * @return
     */
    public long getFirstAvailableIndex() {
        try {
            this.storeReadLock.lock();

            return this.entriesInStore + this.startIndex;
        } finally {
            this.storeReadLock.unlock();
        }
    }

    /**
     * 获取最后提交的日志记录
     *
     * @return
     */
    public LogEntry getLastLogEntry() {
        LogEntry logEntry = this.buffer.lastEntry();
        return logEntry == null ? zeroEntry : logEntry;
    }

    /**
     * 添加日志信息到缓存
     *
     * @param logEntry
     * @return
     */
    public long append(LogEntry logEntry) {
        try {
            this.storeWriteLock.lock();
            //获取索引文件要追加的数据索引位置
            long indexFileLength = this.indexFile.length();
            this.indexFile.seek(indexFileLength);
            //写入数据的索引位置到索引文件
            long dataFileLength = this.dataFile.length();
            this.indexFile.writeLong(dataFileLength);
            //跳转要追加到数据文件的位置
            this.dataFile.seek(dataFileLength);
            //把LogEntry的数据转换Byte
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + 1 + logEntry.getValue().length);
            buffer.putLong(logEntry.getTerm());
            buffer.put(logEntry.getVaueType().toByte());
            buffer.put(logEntry.getValue());
            //写入日志到数据文件里面
            this.dataFile.write(buffer.array());
            //加1
            this.entriesInStore += 1;
            //缓存追加日志
            this.buffer.append(logEntry);

            return this.entriesInStore + this.startIndex - 1;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.storeWriteLock.unlock();
        }
    }

    /**
     * 日志写入指定的位置,后面的日志都抛弃
     *
     * @param logIndex
     * @param logEntry
     */
    public void writeAt(long logIndex, LogEntry logEntry) {

    }

    public static class LogBuffer {

        //读写锁
        private ReentrantReadWriteLock bufferLock;
        //读锁
        private ReentrantReadWriteLock.ReadLock bufferReadLock;
        //写锁
        private ReentrantReadWriteLock.WriteLock bufferWriteLock;

        private long startIndex;
        private int maxSize;

        private List<LogEntry> buffer;

        public LogBuffer(long startIndex, int maxSize) {
            //索引位置
            this.startIndex = startIndex;
            //大小
            this.maxSize = maxSize;
            this.bufferLock = new ReentrantReadWriteLock();
            this.bufferReadLock = this.bufferLock.readLock();
            this.bufferWriteLock = this.bufferLock.writeLock();

            this.buffer = new ArrayList<>();
        }

        /**
         * 获取
         *
         * @return
         */
        public long firstIndex() {
            try {
                this.bufferReadLock.lock();
                return this.startIndex;
            } finally {
                this.bufferReadLock.unlock();
            }
        }

        public void append(LogEntry logEntry) {
            try {
                this.bufferWriteLock.lock();
                this.buffer.add(logEntry);
                if (this.maxSize < this.buffer.size()) {
                    this.buffer.remove(0);
                    //如果移除了缓存里面的数据,要记录一下
                    this.startIndex += 1;
                }
            } finally {
                this.bufferWriteLock.unlock();
            }
        }

        /**
         * 获取最后提交的lastEntry
         *
         * @return
         */
        public LogEntry lastEntry() {
            try {
                this.bufferReadLock.lock();
                if (this.buffer.size() > 0) {
                    return buffer.get(buffer.size() - 1);
                }
                return null;
            } finally {
                this.bufferReadLock.unlock();
            }
        }
    }
}
