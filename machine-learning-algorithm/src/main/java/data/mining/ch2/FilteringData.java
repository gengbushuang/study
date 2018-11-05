package data.mining.ch2;

import org.apache.commons.lang3.StringUtils;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteringData {


    public INDArray load(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        Map<String, String[]> stringMap = new HashMap<>();
        for (String line : lines) {
            String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
            //String[] newStr = new String[split.length - 1];
            stringMap.put(split[0], Arrays.copyOfRange(split, 1, split.length));
//            System.out.println(Arrays.toString(split));
//            for(int i = 1;i<split.length;i++){
//            }
        }

        String key1 = "Hailey";
        String key2 = "Jordyn";

        float manhattan = manhattan(key1, key2, stringMap);
        System.out.println("(" + key1 + "," + key2 + ")曼哈顿距离->" + manhattan);
        float euclidean = euclidean(key1, key2, stringMap);
        System.out.println("(" + key1 + "," + key2 + ")欧式距离->" + euclidean);
        return null;
    }

    public float manhattan(String use1, String use2, Map<String, String[]> stringMap) {
        String[] strings1 = stringMap.get(use1);
        String[] strings2 = stringMap.get(use2);
        float sum = 0.0f;
        for (int i = 0; i < strings1.length; i++) {
            if (StringUtils.isBlank(strings1[i]) || StringUtils.isBlank(strings2[i])) {
                continue;
            }
            sum += Math.abs(Float.parseFloat(strings1[i]) - Float.parseFloat(strings2[i]));
        }
        return sum;
    }

    public float euclidean(String use1, String use2, Map<String, String[]> stringMap) {
        String[] strings1 = stringMap.get(use1);
        String[] strings2 = stringMap.get(use2);
        float sum = 0.0f;
        for (int i = 0; i < strings1.length; i++) {
            if (StringUtils.isBlank(strings1[i]) || StringUtils.isBlank(strings2[i])) {
                continue;
            }
            sum += Math.pow(Float.parseFloat(strings1[i]) - Float.parseFloat(strings2[i]), 2);
        }
        return (float) Math.sqrt(sum);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        FilteringData filteringData = new FilteringData();
        URL resource = FilteringData.class.getResource("input.txt");
        System.out.println(resource);
        System.out.println(resource.toURI().getPath());
        INDArray load = filteringData.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/input.txt");
    }
}
