package mapreduce;

import mapreduce.common.KeyValue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private int nNumber = 100;
    private int nMap = 20;
    private int nReduce = 10;

    public String[] makeInputs(int num) {
        String[] names = new String[num];
        int i = 0;
        for (int f = 0; f < num; f++) {
            String name = String.format("824-mrinput-%d.txt", f);
            names[f] = name;
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(name))) {
                for (; i < (f + 1) * (nNumber / num); i++) {
                    writer.write(String.format("%d\n", i));
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return names;
    }

    public void TestSequentialSingle() {
        String outPath = "C:\\Users\\gbs\\git\\study\\mit-6.824\\data\\mapreduce\\";
        Master.sequential("test", makeInputs(5), 3,
                (file, values) -> {
                    String[] words = values.split("\\n|\\v|\\f|\\r|' '");
                    List<KeyValue> keyValueList = new ArrayList<>(words.length);
                    for (String w : words) {
                        keyValueList.add(new KeyValue(w, ""));
                    }
                    return keyValueList;
                },
                (key, values) -> {
                    for (String v : values) {

                    }
                    return "";
                });
    }

    public static void main(String[] args) {
        new Test().TestSequentialSingle();
    }
}
