package mapreduce.common;

@FunctionalInterface
public interface FunctionReduce {

    public String reduceF(String key, String[] values);
}
