package mapreduce.common;

@FunctionalInterface
public interface FunctionaMap {

    public KeyValue[] mapF(String document, String value);
}
