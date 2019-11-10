package mapreduce;

import mapreduce.common.Common;
import mapreduce.common.FunctionReduce;
import mapreduce.common.FunctionaMap;
import mapreduce.common.JobPhase;

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
}
