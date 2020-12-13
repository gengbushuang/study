package com.retrieval.data;

import com.retrieval.constants.LogConstants;
import com.retrieval.util.ByteBufferSupport;
import com.retrieval.util.CheckUtils;
import com.retrieval.util.IoUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedLogWriter implements LogWriter {

    private final static int PAGE_SIZE = 1024 * 1024;

    private final File file;
    private final long fileNumber;
    private final FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;

    private int fileOffset;

    private int logBlockOffset;

    public MappedLogWriter(File file, long fileNumber) throws IOException {

        CheckUtils.checkNonNull(file, "file is null");

        this.file = file;
        this.fileNumber = fileNumber;

        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, PAGE_SIZE);
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public synchronized void close() throws IOException {

        //对MappedByteBuffer进行释放
        this.flushMappedByteBuffer();

        if (fileChannel.isOpen()) {
            //截断已经写过的文件内容
            fileChannel.truncate(fileOffset);
        }

        IoUtils.closeQuietly(fileChannel);
    }

    private void flushMappedByteBuffer() {
        if (mappedByteBuffer != null) {
            this.fileOffset += mappedByteBuffer.position();
            this.unmap();
        }

        mappedByteBuffer = null;
    }

    private void unmap() {
        ByteBufferSupport.unmap(mappedByteBuffer);
    }

    @Override
    public synchronized void delete() throws IOException {
        this.close();

        file.delete();

    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public long getFileNumber() {
        return fileNumber;
    }

    @Override
    public synchronized void addRecord(DataByte dataByte, boolean flush) throws IOException {

        InputDataByte input = dataByte.input();
        boolean first = true;
        do {
            //剩余空间
            int blockRemaining = LogConstants.LOG_BLOCK_SIZE - logBlockOffset;
            //判断剩余空间是否满足
            if (blockRemaining < LogConstants.LOG_HEADER_SIZE) {
                //要重新刷新现有的数据
                //重新分配文件空间
                capacity(blockRemaining);

                logBlockOffset = 0;
                blockRemaining = LogConstants.LOG_BLOCK_SIZE - logBlockOffset;
            }
            //减去头消息长度
            int blockAvailable = blockRemaining - LogConstants.LOG_HEADER_SIZE;
            boolean last;
            int bodyLen;
            //判断能否写入消息体的长度
            if (input.available() > blockAvailable) {
                //可写入空间小于消息体长度，设置可写入空间为读取长度
                bodyLen = blockAvailable;
                last = false;
            } else {
                //可写入空间大于消息体长度，设置消息体长度为读取长度
                bodyLen = input.available();
                last = true;
            }

            FrameType type;
            if (first && last) {//表示数据完整标识完整数据类型
                type = FrameType.ALL;
            } else if (first) {//表示数据不完整标识开头数据类型
                type = FrameType.FIRST;
            } else if (last) {//表示数据不完整标识末尾数据类型
                type = FrameType.LAST;
            } else {//表示数据不完整标识是中间数据类型
                type = FrameType.MIDDLE;
            }


            writeFrame(type, input.readBytes(bodyLen));
            first = false;
        } while (input.isReadable());

        if (flush) {
            //刷新磁盘
            mappedByteBuffer.force();
        }
    }

    private void capacity(int blockRemaining) throws IOException {
        if (mappedByteBuffer.remaining() < blockRemaining) {
            this.fileOffset += mappedByteBuffer.position();
            this.unmap();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileOffset, PAGE_SIZE);
        }


    }

    private void writeFrame(FrameType type, DataByte body) throws IOException {

        DataByte head = createHeader(type, body);

        capacity(head.getLength() + body.getLength());

        head.getBytes(0, mappedByteBuffer);
        body.getBytes(0, mappedByteBuffer);

        logBlockOffset += LogConstants.LOG_HEADER_SIZE + body.getLength();
    }

    private DataByte createHeader(FrameType type, DataByte dataByte) {

        DataByte header = new DataByte(LogConstants.LOG_HEADER_SIZE);
        OutputDataByte output = header.output();
        output.writeInt(dataByte.getLength());
        output.writeByte((byte) type.getPersistentId());

        return header;
    }
}
