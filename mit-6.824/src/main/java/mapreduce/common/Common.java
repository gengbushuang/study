package mapreduce.common;

public class Common {


    public static void doMap(String jobName, int mapTask, String inFile, int nReduce, FunctionaMap mapF) {

    }

    public static void doReduce(String jobName, int reduceTask, String outFile, int nMap, FunctionReduce reduceF) {

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
