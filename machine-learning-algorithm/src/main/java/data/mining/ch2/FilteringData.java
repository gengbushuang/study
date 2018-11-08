package data.mining.ch2;

import org.apache.commons.lang3.StringUtils;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FilteringData {

    String[] labels = {"Blues Traveler", "Broken Bells", "Deadmau5", "Norah Jones", "Phoenix", "Slightly Stoopid", "The Strokes", "Vampire Weekend"};


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

        String key1 = "Angelica";
        String key2 = "Jordyn";

//        float manhattan = manhattan(key1, key2, stringMap);
//        System.out.println("(" + key1 + "," + key2 + ")曼哈顿距离->" + manhattan);
//        float euclidean = euclidean(key1, key2, stringMap);
//        System.out.println("(" + key1 + "," + key2 + ")欧式距离->" + euclidean);
//
//        float minkowsk = minkowsk(key1, key2, stringMap, 2);
//        System.out.println("(" + key1 + "," + key2 + ")明氏距离->" + minkowsk);
        recommend(key1, stringMap);

        return null;
    }


    public void recommend(String username, Map<String, String[]> stringMap) {

        List<TwoTuple<String, Float>> twoTuples = computeNearestNeighbor(username, stringMap);
        System.out.println(twoTuples);
        String nearest = twoTuples.get(0).first;
        System.out.println(nearest);
        String[] usernames = stringMap.get(username);
        String[] nearests = stringMap.get(nearest);

        List<TwoTuple<String, Float>> recommendations = new ArrayList<>();
        for (int i = 0; i < usernames.length; i++) {
            //查找不在
            if (StringUtils.isBlank(usernames[i]) && StringUtils.isNotBlank(nearests[i])) {
                recommendations.add(new TwoTuple<>(labels[i], Float.parseFloat(nearests[i])));
                continue;
            }
        }

        recommendations.sort(new Comparator<TwoTuple<String, Float>>() {
            @Override
            public int compare(TwoTuple<String, Float> o1, TwoTuple<String, Float> o2) {
                float v = o2.second - o1.second;
                if (v < 0) {
                    return -1;
                } else if (v > 0) {
                    return 1;
                }
                return 0;
            }
        });

        System.out.println(recommendations);
    }

    public List<TwoTuple<String, Float>> computeNearestNeighbor(String username, Map<String, String[]> stringMap) {
        Set<String> usernames = stringMap.keySet();
        List<TwoTuple<String, Float>> twoTuples = new ArrayList<>(stringMap.size());
        for (String user : usernames) {
            if (user.equals(username)) {
                continue;
            }
            float manhattan = manhattan(username, user, stringMap);
            twoTuples.add(new TwoTuple<>(user, new Float(manhattan)));
        }

        twoTuples.sort(new Comparator<TwoTuple<String, Float>>() {
            @Override
            public int compare(TwoTuple<String, Float> o1, TwoTuple<String, Float> o2) {
                float v = o1.second - o2.second;
                if (v < 0) {
                    return -1;
                } else if (v > 0) {
                    return 1;
                }
                return 0;
            }
        });

        return twoTuples;
    }

    /**
     *
     * @param use1
     * @param use2
     * @param stringMap
     * @return
     */
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

    /**
     * 欧几里得距离
     * @param use1
     * @param use2
     * @param stringMap
     * @return
     */
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

    public float minkowsk(String use1, String use2, Map<String, String[]> stringMap, int r) {
        String[] strings1 = stringMap.get(use1);
        String[] strings2 = stringMap.get(use2);
        float sum = 0.0f;
        for (int i = 0; i < strings1.length; i++) {
            if (StringUtils.isBlank(strings1[i]) || StringUtils.isBlank(strings2[i])) {
                continue;
            }
            float abs = Math.abs(Float.parseFloat(strings1[i]) - Float.parseFloat(strings2[i]));
            sum += Math.pow(abs, r);
        }
        return (float) Math.pow(sum, 1.0 / r);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        FilteringData filteringData = new FilteringData();
        URL resource = FilteringData.class.getResource("input.txt");
        System.out.println(resource);
        System.out.println(resource.toURI().getPath());
        INDArray load = filteringData.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/input.txt");
    }
}
