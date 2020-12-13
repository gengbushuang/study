package raft.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.EntryFactory;
import raft.SequenceLogStore;
import raft.message.Entry;
import raft.message.request.NoOpEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class FileLogStore implements SequenceLogStore {

    private Logger logger = LogManager.getLogger(getClass());

    private final static String LOG_INDEX_FILE = ".idx";
    private final static String LOG_DATA_FILE = ".data";

    private final static String FORMAT_FILE = "%020d";

    //数据偏移位置(8字节)+类型标识(4字节)+任期号标识(4字节)
    private final int INDEX_LENGTH_HEAD = Long.BYTES + Integer.BYTES + Integer.BYTES;

    private final int INDES_FILE_SIZE = INDEX_LENGTH_HEAD * INDEX_LENGTH_HEAD;

    private final ConcurrentSkipListMap<Long, String> skipListMap = new ConcurrentSkipListMap();

    private final EntryFactory factory = new EntryFactory();

    private Path dirPath;


    //索引文件
    private RandomAccessFile indexFile;
    //数据文件
    private RandomAccessFile dataFile;

    private long nextLogIndex = 0;

    private int startIndex = 0;

    private LogBuffer logBuffer;

    public FileLogStore(String dirPath) {
        this.dirPath = Paths.get(dirPath);

        if (!Files.isDirectory(this.dirPath)) {
            try {
                Files.createDirectories(this.dirPath);
            } catch (IOException e) {
                logger.error("create file [{}] error", this.dirPath, e);
                throw new IllegalArgumentException("logstore baseDir");
            }
        } else {
            try {
                Map<Long, String> longStringMap = Files.list(this.dirPath)
                        .filter(p -> p.getFileName().toString().endsWith(LOG_INDEX_FILE))
                        .map(p -> p.getFileName().toString().substring(0, 20))
                        .collect(Collectors.toMap(k -> Long.parseLong(k), k -> k));
                skipListMap.putAll(longStringMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            if (skipListMap.isEmpty()) {
                this.nextLogIndex = 0;
                this.indexFile = new RandomAccessFile(this.dirPath.resolve(this.getFileName(this.nextLogIndex, FORMAT_FILE + LOG_INDEX_FILE)).toString(), "rw");
                this.dataFile = new RandomAccessFile(this.dirPath.resolve(this.getFileName(this.nextLogIndex, FORMAT_FILE + LOG_DATA_FILE)).toString(), "rw");
                skipListMap.put(this.nextLogIndex, this.getFileName(this.nextLogIndex, FORMAT_FILE));
            } else {
                Map.Entry<Long, String> entry = skipListMap.lastEntry();
                this.indexFile = new RandomAccessFile(this.dirPath.resolve(entry.getValue() + LOG_INDEX_FILE).toString(), "rw");
                if (this.indexFile.length() == 0) {
                    this.nextLogIndex = 0;
                } else {
                    int offsetToIndext = getOffsetToIndext(this.indexFile.length());
                    this.startIndex = offsetToIndext;
                    this.nextLogIndex = entry.getKey().longValue() + offsetToIndext;
                }
                this.dataFile = new RandomAccessFile(this.dirPath.resolve(entry.getValue() + LOG_DATA_FILE).toString(), "rw");
            }
//            this.indexFile = new RandomAccessFile(this.dirPath.resolve(LOG_INDEX_FILE).toString(), "rw");
//            if (this.indexFile.length() == 0) {
//                this.nextLogIndex = 0;
//            } else {
//                int offsetToIndext = getOffsetToIndext(this.indexFile.length());
//                this.nextLogIndex = offsetToIndext;
//            }
//            this.dataFile = new RandomAccessFile(this.dirPath.resolve(LOG_DATA_FILE).toString(), "rw");
            this.logBuffer = new LogBuffer(INDES_FILE_SIZE);
            this.fillBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fillBuffer() throws IOException {
        if (this.indexFile.length() > 0) {

            this.indexFile.seek(0);
            byte[] indexData = new byte[startIndex * Long.BYTES];
            int indexStart = 0;
            int readStart = 0;
            int indexEnd = startIndex * INDEX_LENGTH_HEAD;
            while (indexStart < indexEnd) {
                readStart += this.indexFile.read(indexData, readStart, Long.BYTES);
                this.indexFile.skipBytes(Long.BYTES);
                indexStart += INDEX_LENGTH_HEAD;
            }

            ByteBuffer indexBuffer = ByteBuffer.wrap(indexData);
            long dataStart = indexBuffer.getLong();
            this.dataFile.seek(dataStart);
            while (indexBuffer.hasRemaining()) {
                long dataEnd = indexBuffer.getLong();
                int dataLen = (int) (dataEnd - dataStart);
                byte[] data = new byte[dataLen];
                this.dataFile.read(data, 0, data.length);
                Entry entry = create(data);
                this.logBuffer.append(entry);
                dataStart = dataEnd;
            }
            long dataEnd = this.dataFile.length();
            int dataLen = (int) (dataEnd - dataStart);
            byte[] data = new byte[dataLen];
            this.dataFile.read(data, 0, data.length);
            Entry entry = create(data);
            this.logBuffer.append(entry);
        }
    }

    private Entry create(byte[] data) {
        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        int typeF = dataBuffer.getInt();
        int indexF = dataBuffer.getInt();
        int termF = dataBuffer.getInt();
        int length = dataBuffer.getInt();
        byte[] values = new byte[length];
        dataBuffer.get(values);
        return factory.create(typeF, indexF, termF, values);
    }

    public boolean isEmpty() {
        return nextLogIndex == 0;
    }

    @Override
    public long getNextLogIndex() {
        return nextLogIndex;
    }

    @Override
    public long append(Entry entry) {
        if (entry.getIndex() != nextLogIndex) {
            throw new IllegalArgumentException(String.format("entry index {%d} not equal nextLogIndex {%d}", entry.getIndex(), nextLogIndex));
        }

        try {
            if (startIndex >= INDES_FILE_SIZE) {
                this.indexFile.close();
                this.dataFile.close();

                this.indexFile = new RandomAccessFile(this.dirPath.resolve(this.getFileName(this.nextLogIndex, FORMAT_FILE + LOG_INDEX_FILE)).toString(), "rw");
                this.dataFile = new RandomAccessFile(this.dirPath.resolve(this.getFileName(this.nextLogIndex, FORMAT_FILE + LOG_DATA_FILE)).toString(), "rw");
                startIndex = 0;
                skipListMap.put(this.nextLogIndex, this.getFileName(this.nextLogIndex, FORMAT_FILE));
            }

            //数据的偏移位置
            long dataOffset = dataFile.length();
            //索引的偏移位置
            long indexOffset = indexFile.length();
            ///////////////索引文件操作
            indexFile.seek(indexOffset);
            //索引文件写入数据物理地址
            indexFile.writeLong(dataOffset);
            //索引文件写入日志类型标识
            indexFile.writeInt(entry.getType());
            //索引文件写入任期号
            indexFile.writeInt(entry.getTerm());
            ///////////////日志文件操作
            dataFile.seek(dataOffset);
            //日志文件写入日志类型标识
            dataFile.writeInt(entry.getType());
            //日志文件写入日志index标识
            dataFile.writeInt(entry.getIndex());
            //日志文件写入任期号标识
            dataFile.writeInt(entry.getTerm());
            //日志文件写入数据
            byte[] value = entry.getValue();
            dataFile.writeInt(value.length);
            dataFile.write(value);

            startIndex++;
            nextLogIndex++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Entry getEntry(Integer index) {
        if (!checkIndex(index)) {
            throw new IndexOutOfBoundsException("index out index=" + index + ">=nextindex=" + nextLogIndex);
        }
        long indexL = index.longValue();
        Map.Entry<Long, String> entry = skipListMap.floorEntry(indexL);

        long relativeOffset = indexL - entry.getKey().longValue();

        long indexOffset = getIndexToOffset(relativeOffset);
        if (index < nextLogIndex - startIndex) {
            Entry entry1 = getHistory(indexOffset, entry);
            return entry1;
        }

        try {
            indexFile.seek(indexOffset);

            long dataOffset = indexFile.readLong();

            dataFile.seek(dataOffset);
            //读取数据文件type标识
            int typeF = dataFile.readInt();
            //读取数据文件index标识
            int indexF = dataFile.readInt();
            //读取数据文件term标识
            int termF = dataFile.readInt();
            //读取数据文件byte长度
            int valueLength = dataFile.readInt();
            byte[] values = new byte[valueLength];
            dataFile.read(values);
            return factory.create(typeF, indexF, termF, values);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Entry[] getListEntrys(Integer fromIndex, Integer toIndex) {
        long fromIndexL = fromIndex.longValue();
        long toIndexL = toIndex.longValue();

        int indexCount = toIndex.intValue() - fromIndex.intValue();
        int indexStartCount = 0;

        Map.Entry<Long, String> fromEntry = skipListMap.floorEntry(fromIndexL);
        Map.Entry<Long, String> toEntry = skipListMap.floorEntry(toIndexL);

        ConcurrentNavigableMap<Long, String> indexSubMap = skipListMap.subMap(fromEntry.getKey(), true, toEntry.getKey(), true);

        long relativeOffset = fromIndexL - fromEntry.getKey().longValue();
        long indexOffset = getIndexToOffset(relativeOffset);
        Entry[] entrys = new Entry[indexCount + 1];
        Set<Map.Entry<Long, String>> subEntrys = indexSubMap.entrySet();
        for (Map.Entry<Long, String> subEntry : subEntrys) {
            RandomAccessFile indexHistoryFile = null;
            RandomAccessFile dataHistoryFile = null;
            try {
                indexHistoryFile = new RandomAccessFile(this.dirPath.resolve(subEntry.getValue() + LOG_INDEX_FILE).toString(), "r");
                dataHistoryFile = new RandomAccessFile(this.dirPath.resolve(subEntry.getValue() + LOG_DATA_FILE).toString(), "r");
                for (; indexOffset < indexHistoryFile.length() && indexStartCount <= indexCount; ++indexStartCount) {
                    indexHistoryFile.seek(indexOffset);
                    long dataOffset = indexHistoryFile.readLong();
                    dataHistoryFile.seek(dataOffset);
                    //读取数据文件type标识
                    int typeF = dataHistoryFile.readInt();
                    //读取数据文件index标识
                    int indexF = dataHistoryFile.readInt();
                    //读取数据文件term标识
                    int termF = dataHistoryFile.readInt();
                    //读取数据文件byte长度
                    int valueLength = dataHistoryFile.readInt();
                    byte[] values = new byte[valueLength];
                    dataHistoryFile.read(values);
                    Entry entry = factory.create(typeF, indexF, termF, values);
                    entrys[indexStartCount] = entry;
                    indexOffset += INDEX_LENGTH_HEAD;
                }
                indexOffset = 0;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    indexHistoryFile.close();
                    dataHistoryFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return entrys;
    }

    private Entry getHistory(long indexOffset, Map.Entry<Long, String> entry) {
        RandomAccessFile indexHistoryFile = null;
        RandomAccessFile dataHistoryFile = null;
        try {
            indexHistoryFile = new RandomAccessFile(this.dirPath.resolve(entry.getValue() + LOG_INDEX_FILE).toString(), "r");
            dataHistoryFile = new RandomAccessFile(this.dirPath.resolve(entry.getValue() + LOG_DATA_FILE).toString(), "r");
            indexHistoryFile.seek(indexOffset);

            long dataOffset = indexHistoryFile.readLong();

            dataHistoryFile.seek(dataOffset);
            //读取数据文件type标识
            int typeF = dataHistoryFile.readInt();
            //读取数据文件index标识
            int indexF = dataHistoryFile.readInt();
            //读取数据文件term标识
            int termF = dataHistoryFile.readInt();
            //读取数据文件byte长度
            int valueLength = dataHistoryFile.readInt();
            byte[] values = new byte[valueLength];
            dataHistoryFile.read(values);
            return factory.create(typeF, indexF, termF, values);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (indexHistoryFile != null) {
                try {
                    indexHistoryFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dataHistoryFile != null) {
                try {
                    dataHistoryFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Entry getLastEntry() {
        if (this.isEmpty()) {
            return new NoOpEntry(0, 0);
        }
        if (!logBuffer.isEmpty()) {
            return logBuffer.getLastEntry();
        }
        return this.getEntry((int) (nextLogIndex - 1));
    }

    @Override
    public void close() throws Exception {
        try {
            this.dataFile.close();
            this.indexFile.close();
        } catch (IOException e) {
            this.logger.error("failed to close data/index file(s)", e);
            throw e;
        }
    }


    private long getIndexToOffset(int index) {
        return index * INDEX_LENGTH_HEAD;
    }

    private long getIndexToOffset(long index) {
        return index * INDEX_LENGTH_HEAD;
    }

    private int getOffsetToIndext(long offset) {
        return (int) (offset / INDEX_LENGTH_HEAD);
    }

    private boolean checkIndex(int index) {
        return index < nextLogIndex;
    }

    private String getFileName(long logIndex, String format) {
        return String.format(format, logIndex);
    }


    public static class LogBuffer {

        private Entry[] bufferEntry;

        private int bufferStartIndex;

        private int bufferLength;

        public LogBuffer(int bufferLength) {
            this.bufferStartIndex = 0;
            this.bufferLength = bufferLength;
            this.bufferEntry = new Entry[bufferLength];
        }


        public void append(Entry entry) {
            if (bufferStartIndex >= bufferLength) {
                bufferStartIndex = 0;
            }
            bufferEntry[bufferStartIndex] = entry;
            bufferStartIndex++;
        }

        public Entry getEntry(int index) {
            return bufferEntry[index];
        }

        public boolean isEmpty() {
            return bufferStartIndex == 0;
        }

        public Entry getLastEntry() {
            if (isEmpty()) {
                return null;
            }
            return bufferEntry[bufferStartIndex - 1];
        }
    }
}
