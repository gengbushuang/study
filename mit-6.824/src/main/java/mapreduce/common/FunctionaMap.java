package mapreduce.common;

import java.util.List;

@FunctionalInterface
public interface FunctionaMap {

    public List<KeyValue> mapF(String document, String value);
}
