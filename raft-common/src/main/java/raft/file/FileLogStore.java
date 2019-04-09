package raft.file;

import com.sun.corba.se.impl.logging.IORSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.BinaryUtils;
import raft.LogEntry;
import raft.LogValueType;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileLogStore {
    private Logger logger;

    private final static String LOG_INDEX_FILE = "store.idx";
    private final static String LOG_STORE_FILE = "store.data";
    private final static String LOG_START_INDEX_FILE = "store.sti";

    private static final String LOG_INDEX_FILE_BAK = "store.idx.bak";
    private static final String LOG_STORE_FILE_BAK = "store.data.bak";
    private static final String LOG_START_INDEX_FILE_BAK = "store.sti.bak";

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
        this.logger = LogManager.getLogger(getClass());

        this.storeLock = new ReentrantReadWriteLock();
        this.storeReadLock = this.storeLock.readLock();
        this.storeWriteLock = this.storeLock.writeLock();
        this.logContainer = Paths.get(logContainer).resolve("logstore");

        if(!Files.isDirectory(this.logContainer)){
            try {
                Files.createDirectory(this.logContainer);
            }catch (Exception error){
                throw new IllegalArgumentException("logstore baseDir");
            }
        }

        this.bufferSize = bufferSize;

        try {
            //数据下标
            this.indexFile = new RandomAccessFile(this.logContainer.resolve(LOG_INDEX_FILE).toString(), "rw");
            //对应的数据
            this.dataFile = new RandomAccessFile(this.logContainer.resolve(LOG_STORE_FILE).toString(), "rw");
            //开始下标文件
            this.startIndexFile = new RandomAccessFile(this.logContainer.resolve(LOG_START_INDEX_FILE).toString(), "rw");
            //查看文件里面是否有数据
            if (this.startIndexFile.length() == 0) {
                this.startIndex = 1;
                this.startIndexFile.writeLong(this.startIndex);
            } else {
                this.startIndex = this.startIndexFile.readLong();
            }
            //下标数
            this.entriesInStore = this.indexFile.length() / Long.BYTES;
            this.buffer = new LogBuffer(this.entriesInStore > this.bufferSize ? (this.entriesInStore + this.startIndex - this.bufferSize) : this.startIndex, this.bufferSize);
            this.fillBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从文件里面加入对应缓存大小数据
     *
     * @throws IOException
     */
    private void fillBuffer() throws IOException {
        //缓存下标
        long startIndex = this.buffer.firstIndex();
        //索引最后下标
        long indexFileSize = this.indexFile.length();
        //如果索引文件大小不为0，就从缓存下标到最后文件下标缓存到内存中
        if (indexFileSize > 0) {
            //计算从索引文件那个下标开始
            long indexPosition = (startIndex - this.startIndex) * Long.BYTES;
            this.indexFile.seek(indexPosition);
            //把索引文件数据读到byte数组里面
            byte[] indexData = new byte[(int) (indexFileSize - indexPosition)];
            this.read(this.indexFile, indexData);
            ByteBuffer indexBuffer = ByteBuffer.wrap(indexData);
            //循环从byte数组缓存中拿取索引下标
            //在根据下标获取对应的真正的数据
            long dataStart = indexBuffer.getLong();
            this.dataFile.seek(dataStart);
            while (indexBuffer.hasRemaining()) {
                long dataEnd = indexBuffer.getLong();
                LogEntry logEntry = this.readEntry((int) (dataEnd - dataStart));
                this.buffer.append(logEntry);
                dataStart = dataEnd;
            }
            long dataEnd = this.dataFile.length();
            this.buffer.append(this.readEntry((int) (dataEnd - dataStart)));
        }
    }

    /**
     * 下一个下标值
     *
     *
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

    private LogEntry readEntry(int size) {
        byte[] entryData = new byte[size];
        this.read(this.dataFile, entryData);
        ByteBuffer entryBuffer = ByteBuffer.wrap(entryData);
        long term = entryBuffer.getLong();
        byte valueType = entryBuffer.get();

        return new LogEntry(term, Arrays.copyOfRange(entryData, entryBuffer.position(), entryBuffer.limit()), LogValueType.fromByte(valueType));
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
            //获取索引文件最末尾的标记
            long indexFileLength = this.indexFile.length();
            this.indexFile.seek(indexFileLength);
            //获取数据文件最末尾标记
            long dataFileLength = this.dataFile.length();
            //把要写入数据文件的标记写到索引文件里面建立对应关系
            this.indexFile.writeLong(dataFileLength);
            //跳转到数据文件标记
            this.dataFile.seek(dataFileLength);
            //把LogEntry的数据转换Byte
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + 1 + logEntry.getValue().length);
            buffer.putLong(logEntry.getTerm());
            buffer.put(logEntry.getVaueType().toByte());
            buffer.put(logEntry.getValue());
            //数据追加到数据文件里面
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
        this.throwWhenNotInRange(logIndex);
        this.storeWriteLock.lock();
        try {
            //计算下标位置
            long index = logIndex - this.startIndex + 1;
            //logEntry转换byte
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + 1 + logEntry.getValue().length);
            buffer.putLong(logEntry.getTerm());
            buffer.put(logEntry.getVaueType().toByte());
            buffer.put(logEntry.getValue());
            //获取数据文件的长度
            long dataPosition = this.dataFile.length();
            //计算索引文件下标位置
            long indexPosition = (index - 1) * Long.BYTES;
            //判断索引文件下标数据小于索引文件长度
            if (indexPosition < this.indexFile.length()) {
                //索引文件下标跳转
                this.indexFile.seek(indexPosition);
                dataPosition = this.indexFile.readLong();
            }
            this.indexFile.seek(indexPosition);
            this.dataFile.seek(dataPosition);
            this.indexFile.writeLong(dataPosition);
            this.dataFile.write(buffer.array());

            if (this.indexFile.length() > this.indexFile.getFilePointer()) {
                this.indexFile.seek(this.indexFile.getFilePointer());
            }

            if (this.dataFile.length() > this.dataFile.getFilePointer()) {
                this.dataFile.seek(this.dataFile.getFilePointer());
            }
            //清除下标到结尾的缓存
            if (index <= this.entriesInStore) {
                this.buffer.trim(logIndex);
            }

            this.buffer.append(logEntry);
            this.entriesInStore = index;

        } catch (IOException e) {
            this.logger.error("failed to write a log entry at a specific index to store", e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.storeWriteLock.unlock();
        }

    }


    public LogEntry getLogEntryAt(long logIndex) {
        this.throwWhenNotInRange(logIndex);
        long index = 0;
        this.storeReadLock.lock();
        try {
            //计算真正的下标
            index = logIndex - this.startIndex + 1;
            //超出下标
            if (index > this.entriesInStore) {
                return null;
            }

        } finally {
            this.storeReadLock.unlock();
        }

        LogEntry entry = this.buffer.entryAt(index);
        if (entry != null) {
            return entry;
        }
        //如果缓存里面没有，就重文件里面获取
        this.storeWriteLock.lock();
        try {
            long indexPosition = (index - 1) * Long.BYTES;
            //找到索引文件里面对应数据下标
            this.indexFile.seek(indexPosition);
            //从索引文件里面获取数据文件对应的开始标记
            long dataPosition = this.indexFile.readLong();
            //从索引文件里面获取数据文件对应的结束标记
            long endDataPostion = this.indexFile.readLong();
            this.dataFile.seek(dataPosition);
            byte[] logData = new byte[(int) (endDataPostion - dataPosition)];
            this.read(this.dataFile, logData);
            //第一个参数从0开始取long值，0－7下标
            //第二个参数从8+1开始获取数据，9-数组长度
            //第三个参数从8开始，8下标
            return new LogEntry(BinaryUtils.bytesToLong(logData, 0), Arrays.copyOfRange(logData, Long.BYTES + 1, logData.length), LogValueType.fromByte(logData[Long.BYTES]));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.storeWriteLock.unlock();
        }

    }

    private void read(RandomAccessFile dataFile, byte[] buffer) {
        try {
            //累加读取多少个字节
            int offset = 0;
            //文件读取多少个字节
            int bytesRead = 0;
            while (offset < buffer.length && (bytesRead = dataFile.read(buffer, offset, buffer.length - offset)) != -1) {
                offset += bytesRead;
            }

            if (offset < buffer.length) {
                throw new RuntimeException("bad file, insufficient file data for reading");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private void backup() {
        try {
            //删除文件备份
            Files.deleteIfExists(this.logContainer.resolve(LOG_INDEX_FILE_BAK));
            Files.deleteIfExists(this.logContainer.resolve(LOG_STORE_FILE_BAK));
            Files.deleteIfExists(this.logContainer.resolve(LOG_START_INDEX_FILE_BAK));
            //重新备份文件
            Files.copy(this.logContainer.resolve(LOG_INDEX_FILE), this.logContainer.resolve(LOG_INDEX_FILE_BAK));
            Files.copy(this.logContainer.resolve(LOG_STORE_FILE), this.logContainer.resolve(LOG_STORE_FILE_BAK));
            Files.copy(this.logContainer.resolve(LOG_START_INDEX_FILE), this.logContainer.resolve(LOG_START_INDEX_FILE_BAK));
        } catch (IOException e) {
            throw new RuntimeException("failed to create a backup folder");
        }
    }

    /**
     * 判断索引是否在范围内
     *
     * @param index
     */
    private void throwWhenNotInRange(long index) {
        this.storeReadLock.lock();
        try {
            if (index < this.startIndex) {
                throw new IllegalArgumentException("logIndex out of range");
            }
        } finally {
            this.storeReadLock.unlock();
        }
    }

    /**
     * 获取开始下标
     *
     * @return
     */
    public long getStartIndex() {
        this.storeReadLock.lock();
        try {
            return this.startIndex;
        } finally {
            storeReadLock.unlock();
        }
    }

    public LogEntry[] getLogEntries(long startIndex, long endIndex) {
        this.throwWhenNotInRange(startIndex);

        long start, adjustedEnd, targetEndIndex;
        this.storeReadLock.lock();
        try {
            start = startIndex - this.startIndex;
            adjustedEnd = endIndex - this.startIndex;
            adjustedEnd = adjustedEnd > this.entriesInStore ? this.entriesInStore : adjustedEnd;
            targetEndIndex = endIndex > this.entriesInStore + this.startIndex + 1 ? this.entriesInStore + this.startIndex + 1 : endIndex;
        } finally {
            this.storeReadLock.unlock();
        }

        try {
            LogEntry[] entries = new LogEntry[(int) (adjustedEnd - start)];
            if (entries.length == 0) {
                return entries;
            }

            long bufferFirstIndex = this.buffer.fill(startIndex, targetEndIndex, entries);
            if (startIndex < bufferFirstIndex) {

                this.storeWriteLock.lock();
                try {
                    long end = bufferFirstIndex - this.startIndex;
                    this.indexFile.seek(start * Long.BYTES);
                    int n = (int) (end - start);
                    long dataStart = this.indexFile.readLong();
                    for (int i = 0; i < n; ++i) {
                        long dataEnd = this.indexFile.readLong();
                        int dataSize = (int) (dataEnd - dataStart);
                        byte[] logData = new byte[dataSize];
                        this.dataFile.seek(dataStart);
                        this.read(this.dataFile, logData);
                        entries[i] = new LogEntry(BinaryUtils.bytesToLong(logData, 0), Arrays.copyOfRange(logData, Long.BYTES + 1, logData.length), LogValueType.fromByte(logData[Long.BYTES]));
                        dataStart = dataEnd;
                    }
                } finally {
                    this.storeWriteLock.unlock();
                }

            }
            return entries;
        } catch (IOException e) {
            this.logger.error("failed to read entries from store", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void close() {
        this.storeWriteLock.lock();
        try {
            this.dataFile.close();
            this.indexFile.close();
            this.startIndexFile.close();
        } catch (IOException e) {
            this.logger.error("failed to close data/index file(s)", e);
        } finally {
            this.storeWriteLock.unlock();
        }
    }

    public static class LogBuffer {

        //读写锁
        private ReentrantReadWriteLock bufferLock;
        //读锁
        private ReentrantReadWriteLock.ReadLock bufferReadLock;
        //写锁
        private ReentrantReadWriteLock.WriteLock bufferWriteLock;

        private long bufferStartIndex;
        private int maxSize;

        private List<LogEntry> buffer;

        public LogBuffer(long startIndex, int maxSize) {
            //缓存开始下标
            this.bufferStartIndex = startIndex;
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
                return this.bufferStartIndex;
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
                    this.bufferStartIndex += 1;
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

        public LogEntry entryAt(long index) {
            this.bufferReadLock.lock();
            try {
                //要获取的下标减去缓存的开始下标
                int indexWithinBuffer = (int) (index - this.bufferStartIndex);
                //大于等于缓存大小或者小于0，返回null
                if (indexWithinBuffer >= this.buffer.size() || indexWithinBuffer < 0) {
                    return null;
                }
                //获取日志缓存数据
                return this.buffer.get(indexWithinBuffer);
            } finally {
                this.bufferReadLock.unlock();
            }
        }

        public long fill(long start, long end, LogEntry[] entries) {
            this.bufferReadLock.lock();
            try {
                //如果结尾下标小于缓存起始下标，就返回缓存起始下标
                if (end < this.bufferStartIndex) {
                    return this.bufferStartIndex;
                }

                int offset = (int) (start - this.bufferStartIndex);
                //如果开始下标大于缓存起始下标
                if (offset > 0) {
                    //计算要获取的多少个数据
                    int n = (int) (end - start);
                    //
                    for (int i = 0; i < n; ++i, ++offset) {
                        entries[i] = this.buffer.get(offset);
                    }
                } else {
                    int n = (int) (end - this.bufferStartIndex);
                    offset *= -1;
                    for (int i = 0; i < n; ++i, ++offset) {
                        entries[offset] = this.buffer.get(i);
                    }
                }
                return this.bufferStartIndex;
            } finally {
                this.bufferReadLock.unlock();
            }
        }

        /**
         * 清空fromIndex下标后面的缓存
         *
         * @param fromIndex
         */
        public void trim(long fromIndex) {
            this.bufferWriteLock.lock();
            try {
                int index = (int) (fromIndex - this.bufferStartIndex);
                if (index < this.buffer.size()) {
                    this.buffer.subList(index, this.buffer.size()).clear();
                }

            } finally {
                this.bufferWriteLock.unlock();
            }

        }

        public void trim(long fromIndex,long endIndex){
            this.bufferWriteLock.lock();
            try{
                int index = (int)(fromIndex-this.bufferStartIndex);
                int end = (int)(endIndex-this.bufferStartIndex);
                if(index<this.buffer.size()){
                    this.buffer.subList(index,end).clear();
                }

            }finally {
                this.bufferWriteLock.unlock();
            }
        }
    }
}
