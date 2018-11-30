package data.mining.ch2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nd4j.shade.jackson.core.type.TypeReference;
import org.nd4j.shade.jackson.databind.ObjectMapper;

public class Recommender {

   private static Comparator<TwoTuple<String, Double>> TwoTupleSort = new Comparator<TwoTuple<String, Double>>() {
        @Override
        public int compare(TwoTuple<String, Double> o1, TwoTuple<String, Double> o2) {
            double v = o2.second - o1.second;
            if (v < 0) {
                return -1;
            } else if (v > 0) {
                return 1;
            }
            return 0;
        }
    };

    int k = 1;

    public HashMap<String, HashMap<String, String>> load(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        TypeReference typeReference = new TypeReference<HashMap<String, HashMap<String, String>>>() {
        };

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, HashMap<String, String>> hashMap = mapper.readValue(bytes, typeReference);

        return hashMap;
    }

    public void recommend(String userName, HashMap<String, HashMap<String, String>> userMap) {
        HashMap<String, Double> recommendations = new HashMap<>();

        List<TwoTuple<String, Double>> nearest = computeNearestNeighbor(userName, userMap);

        HashMap<String, String> userRatings = userMap.get(userName);

        double totalDistance = 0.0d;
        //相加topk的皮尔逊系数
        for (int i = 0; i <= k; i++) {
            totalDistance += nearest.get(i).second;
        }
        //计算topk的皮尔系数占比影响
        for (int i = 0; i <= k; i++) {
            double weight = nearest.get(i).second / totalDistance;
            String name = nearest.get(i).first;

            HashMap<String, String> neighborRatings = userMap.get(name);
            for (Entry<String, String> entry : neighborRatings.entrySet()) {
                if (!userRatings.containsKey(entry.getKey())) {
                    if (!recommendations.containsKey(entry.getKey())) {
                        double v = weight + Double.parseDouble(entry.getValue());
                        recommendations.put(entry.getKey(), v);
                    } else {
                        double v = recommendations.get(entry.getKey()) + weight + Double.parseDouble(entry.getValue());
                        recommendations.put(entry.getKey(), v);
                    }
                }
            }
        }

        List<TwoTuple<String, Double>> recommendationsList = new ArrayList<>();
        for (Entry<String, Double> entry : recommendations.entrySet()) {
            recommendationsList.add(new TwoTuple(entry.getKey(), entry.getValue()));
        }
        recommendationsList.sort(TwoTupleSort);


        System.out.println(recommendationsList);
    }

    public List<TwoTuple<String, Double>> computeNearestNeighbor(String userName, HashMap<String, HashMap<String, String>> userMap) {
        List<TwoTuple<String, Double>> twoTuples = new ArrayList<>(userMap.size());
        for (Entry<String, HashMap<String, String>> entry : userMap.entrySet()) {
            if (!userName.equals(entry.getKey())) {
                double distance = pearson(userMap.get(userName), entry.getValue());
                twoTuples.add(new TwoTuple<>(entry.getKey(), distance));
            }
        }
        twoTuples.sort(TwoTupleSort);

        return twoTuples;
    }

    public double pearson(HashMap<String, String> user1, HashMap<String, String> user2) {

        float sum_xy = 0;
        float sum_x = 0;
        float sum_y = 0;
        float sum_x2 = 0;
        float sum_y2 = 0;
        int n = 0;

        for (Map.Entry<String, String> entry : user1.entrySet()) {
            if (!user2.containsKey(entry.getKey())) {
                continue;
            }
            n++;
            float x = Float.parseFloat(entry.getValue());
            float y = Float.parseFloat(user2.get(entry.getKey()));

            // x*y的累加之和
            sum_xy += x * y;
            // x的累加之和
            sum_x += x;
            // y的累加之和
            sum_y += y;
            // x的平方累加之和
            sum_x2 += Math.pow(x, 2);
            // y的平方累加之和
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

    public static void main(String[] args) throws URISyntaxException, IOException {
        Recommender recommender = new Recommender();
        URL resource = Recommender.class.getResource("data.txt");
        System.out.println(resource);
        System.out.println(resource.toURI().getPath());
        HashMap<String, HashMap<String, String>> mapHashMap = recommender.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/data.txt");
        recommender.recommend("Hailey", mapHashMap);

    }
}
