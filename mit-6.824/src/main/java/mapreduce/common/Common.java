package mapreduce.common;

import com.alibaba.fastjson.JSON;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Common {


    public static void doMap(String jobName, int mapTask, String inFile, int nReduce, FunctionaMap mapF) throws IOException {
        Path path = Paths.get(inFile);
        byte[] data = Files.readAllBytes(path);

        List<KeyValue> keyValueList = mapF.mapF(inFile, new String(data));
        List<BufferedWriter> bufferedWriterList = new ArrayList<>(nReduce);
        for (int i = 0; i < nReduce; i++) {
            String reduceName = Common.reduceName(jobName, mapTask, i);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(reduceName));
            //FileChannel channel = FileChannel.open(Paths.get(reduceName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            bufferedWriterList.add(bufferedWriter);
        }

        for (KeyValue keyValue : keyValueList) {
            int r = ihash32a(keyValue.getKey()) % nReduce;
//            FileChannel fileChannel = fileChannelList.get(r);
            BufferedWriter bufferedWriter = bufferedWriterList.get(r);
            bufferedWriter.write(String.format("%s\n",JSON.toJSONString(keyValue)));
//            fileChannel.write(ByteBuffer.wrap((JSON.toJSONString(keyValue) + "\n").getBytes()));
        }

        for (BufferedWriter bufferedWriter : bufferedWriterList) {
            bufferedWriter.flush();
            bufferedWriter.close();
        }
//        for (FileChannel fileChannel : fileChannelList) {
//            fileChannel.close();
//        }
    }

    private static int ihash32a(String s) {
        int FNV_prime = 16777619;
        long hash = 2166136261L;
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            hash = hash ^ b;
            hash = (hash * FNV_prime) & Integer.MAX_VALUE;
        }
        return (int) hash;
    }

    public static void doReduce(String jobName, int reduceTask, String outFile, int nMap, FunctionReduce reduceF) throws IOException {

        Map<String, List<String>> keyValueMap = new HashMap<>();
        for (int i = 0; i < nMap; i++) {
            String reduceName = Common.reduceName(jobName, i, reduceTask);
            Path path = Paths.get(reduceName);
            List<String> readAllLines = Files.readAllLines(path);
            for (String line : readAllLines) {
                KeyValue keyValue = JSON.parseObject(line, KeyValue.class);
                List<String> valueList;
                if (keyValueMap.containsKey(keyValue.getKey())) {
                    valueList = keyValueMap.get(keyValue.getKey());
                } else {
                    valueList = new ArrayList<>();
                    keyValueMap.put(keyValue.getKey(), valueList);
                }
                valueList.add(keyValue.getValue());
            }
        }

        Set<String> keys = keyValueMap.keySet();
        List<String> tmp = new ArrayList<>(keys);
        Collections.sort(tmp);

        Path path = Paths.get(outFile);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String k : tmp) {
                writer.write(String.format("%s\n", JSON.toJSONString(new KeyValue(k, reduceF.reduceF(k, keyValueMap.get(k))))));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String reduceName(String jobName, int mapTask, int reduceTask) {
        return "mrtmp." + jobName + "-" + mapTask + "-" + reduceTask;
    }

    public static String mergeName(String jobName, int reduceTask) {
        return "mrtmp." + jobName + "-res-" + reduceTask;
    }

    public void schedule(String jobName, String[] mapFiles, int nReduce, JobPhase phase) {
        int ntasks;
        int n_other;
        switch (phase) {
            case MAP_PHASE:
                ntasks = mapFiles.length;
                n_other = nReduce;
                break;
            case REDUCE_PHASE:
                ntasks = nReduce;
                n_other = mapFiles.length;
                break;
        }
    }

}
