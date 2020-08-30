package mapreduce;

import mapreduce.common.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class WcTest {


    public static void main(String[] args) {
        args = new String[]{"C:\\Users\\gbs\\git\\study\\mit-6.824\\data\\mapreduce\\wc1.txt"};
        String outPath = "C:\\Users\\gbs\\git\\study\\mit-6.824\\data\\mapreduce\\";
        Master.sequential("wcseq", args, 3,
                (file, values) -> {
                    String[] words = values.split("\\n|\\v|\\f|\\r| ");
                    List<KeyValue> keyValueList = new ArrayList<>(words.length);
                    for (String w : words) {
                        keyValueList.add(new KeyValue(w, "1"));
                    }
                    return keyValueList;
                },
                (key, values) -> {
                    int sum = 0;
                    for (String v : values) {
                        sum++;
                    }
                    return String.valueOf(sum);
                });
    }
}
