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

        String key1 = "Jordyn";
        String key2 = "Sam";
//        double cosine = cosine(key1, key2, stringMap);
//        System.out.println("(" + key1 + "," + key2 + ")余玄相关系数->" + cosine);
        double pearson = pearson(key1, key2, stringMap);
        System.out.println("(" + key1 + "," + key2 + ")皮尔逊相关系数->" + pearson);
//        float manhattan = manhattan(key1, key2, stringMap);
//        System.out.println("(" + key1 + "," + key2 + ")曼哈顿距离->" + manhattan);
//        float euclidean = euclidean(key1, key2, stringMap);
//        System.out.println("(" + key1 + "," + key2 + ")欧式距离->" + euclidean);
//
//        float minkowsk = minkowsk(key1, key2, stringMap, 2);
//        System.out.println("(" + key1 + "," + key2 + ")明氏距离->" + minkowsk);
        //recommend(key1, stringMap);

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
     * 曼哈頓距離(|x1-x2|+|y1-y2|)
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
     * 欧几里得距离(\sqrt{a^2+b^2})
     * a平方加上b平方开方
     *
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

    /**
     * 明氏距離
     *
     * @param use1
     * @param use2
     * @param stringMap
     * @param r
     * @return
     */
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

    /**
     * 皮尔逊相似度
     *
     * @param use1
     * @param use2
     * @param stringMap
     * @return
     */
    public double pearson(String use1, String use2, Map<String, String[]> stringMap) {
        String[] strings1 = stringMap.get(use1);
        String[] strings2 = stringMap.get(use2);
        float sum_xy = 0;
        float sum_x = 0;
        float sum_y = 0;
        float sum_x2 = 0;
        float sum_y2 = 0;
        int n = 0;

        for (int i = 0; i < strings1.length; i++) {
            if (StringUtils.isBlank(strings1[i]) || StringUtils.isBlank(strings2[i])) {
                continue;
            }
            n++;
            float x = Float.parseFloat(strings1[i]);
            float y = Float.parseFloat(strings2[i]);
            //x*y的累加之和
            sum_xy += x * y;
            //x的累加之和
            sum_x += x;
            //y的累加之和
            sum_y += y;
            //x的平方累加之和
            sum_x2 += Math.pow(x, 2);
            //y的平方累加之和
            sum_y2 += Math.pow(y, 2);
        }
        if (n == 0) {
            return 0;
        }
        double denominator = Math.sqrt(sum_x2 - (Math.pow(sum_x, 2) / n)) * Math.sqrt(sum_y2 - (Math.pow(sum_y, 2) / n));
        if (denominator == 0) {
            return 0;
        }
        double r = (sum_xy - ((sum_x * sum_y) / n)) / denominator;
        return r;
    }

    /**
     * 余玄相似度
     * cos(x,y)=(x*y) / (||x||*||y||)
     *
     * @param use1
     * @param use2
     * @param stringMap
     * @return
     */
    public double cosine(String use1, String use2, Map<String, String[]> stringMap) {
        String[] strings1 = stringMap.get(use1);
        String[] strings2 = stringMap.get(use2);
        float sum_x2 = 0;
        float sum_y2 = 0;
        float sum_xy = 0;
        for (int i = 0; i < strings1.length; i++) {
            float x;
            float y;
            if (StringUtils.isBlank(strings1[i])) {
                x = 0f;
            } else {
                x = Float.parseFloat(strings1[i]);
            }
            if (StringUtils.isBlank(strings2[i])) {
                y = 0;
            } else {
                y = Float.parseFloat(strings2[i]);
            }
            sum_xy += x * y;
            sum_x2 += Math.pow(x, 2);
            sum_y2 += Math.pow(y, 2);
        }
        double denominator = Math.sqrt(sum_x2) * Math.sqrt(sum_y2);

        return sum_xy / denominator;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        FilteringData filteringData = new FilteringData();
        URL resource = FilteringData.class.getResource("input.txt");
        System.out.println(resource);
        System.out.println(resource.toURI().getPath());
        INDArray load = filteringData.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/input.txt");
    }
}
