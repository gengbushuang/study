package mapreduce.common;

import java.util.List;

@FunctionalInterface
public interface FunctionReduce {

    public String reduceF(String key, List<String> values);
}
