package mapreduce;

import com.alibaba.fastjson.JSON;
import mapreduce.common.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Master {

    private String jobName;

    private String[] files;

    private int nReduce;


    public String getJobName() {
        return jobName;
    }

    public String[] getFiles() {
        return files;
    }

    public static void sequential(String jobName, String[] files, int nReduce, FunctionaMap mapF, FunctionReduce reduceF) {
        Master master = new Master();
        master.run(jobName, files, nReduce, phase -> {
            switch (phase) {
                case MAP_PHASE:
                    for (int i = 0; i < master.files.length; i++) {
                        Common.doMap(master.jobName, i, master.files[i], master.nReduce, mapF);
                    }
                    break;
                case REDUCE_PHASE:
                    for (int i = 0; i < master.nReduce; i++) {
                        Common.doReduce(master.jobName, i, Common.mergeName(master.jobName, i), master.files.length, reduceF);
                    }
                    break;
            }
        });
    }

    public void run(String jobName, String[] files, int nReduce, Consumer<JobPhase> schedule) {
        this.jobName = jobName;
        this.files = files;
        this.nReduce = nReduce;

        schedule.accept(JobPhase.MAP_PHASE);
        schedule.accept(JobPhase.REDUCE_PHASE);
    }

    //合并排序
    public void merge() {
        Map<String, String> kvs = new HashMap<>();
        for (int i = 0; i < this.nReduce; i++) {
            String p = Common.mergeName(this.jobName, i);
            try {
                List<String> dec = Files.readAllLines(Paths.get(p));
                for (String json : dec) {
                    KeyValue kv = JSON.parseObject(json, KeyValue.class);
                    kvs.put(kv.getKey(), kv.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
