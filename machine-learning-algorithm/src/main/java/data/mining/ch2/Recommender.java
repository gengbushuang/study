package data.mining.ch2;

import org.apache.commons.lang3.StringUtils;
import org.nd4j.shade.jackson.core.type.TypeReference;
import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

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

    private int k;

    private int n;

    private Map<String, String> bookTitle = new HashMap<>();

    public Recommender() {
        this(3, 5);
    }

    public Recommender(int k, int n) {
        this.k = k;
        this.n = n;
    }

    public HashMap<String, HashMap<String, String>> load(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        TypeReference typeReference = new TypeReference<HashMap<String, HashMap<String, String>>>() {
        };

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, HashMap<String, String>> hashMap = mapper.readValue(bytes, typeReference);

        return hashMap;
    }

    public List<TwoTuple<String, Double>> recommend(String userName, HashMap<String, HashMap<String, String>> userMap) {
        HashMap<String, Double> recommendations = new HashMap<>();

        List<TwoTuple<String, Double>> nearest = computeNearestNeighbor(userName, userMap);

        HashMap<String, String> userRatings = userMap.get(userName);
        //System.out.println(nearest);
        double totalDistance = 0.0d;
        //累加相邻K的皮尔系数
        for (int i = 0; i <= k; i++) {
            totalDistance += nearest.get(i).second;
        }
        System.out.println("totalDistance--->" + totalDistance);
        //计算topk的皮尔系数占比影响
        for (int i = 0; i <= k; i++) {
            //
            double weight = nearest.get(i).second / totalDistance;
            String name = nearest.get(i).first;
            //获取相邻的评分信息
            HashMap<String, String> neighborRatings = userMap.get(name);
            for (Entry<String, String> entry : neighborRatings.entrySet()) {
                //判断跟相邻用户否有相同的评分
                if (!userRatings.containsKey(entry.getKey())) {
                    //累积相同评分皮尔系数权重
                    double v;
                    if (!recommendations.containsKey(entry.getKey())) {
                        v = weight * Double.parseDouble(entry.getValue());
                    } else {
                        v = recommendations.get(entry.getKey()) + weight * Double.parseDouble(entry.getValue());
                    }
                    recommendations.put(entry.getKey(), v);
                }
            }
        }

        List<TwoTuple<String, Double>> recommendationsList = new ArrayList<>();
        String id;
        for (Entry<String, Double> entry : recommendations.entrySet()) {
            if (bookTitle.containsKey(entry.getKey())) {
                id = bookTitle.get(entry.getKey());
            } else {
                id = entry.getKey();
            }
            recommendationsList.add(new TwoTuple(id, entry.getValue()));
        }
        recommendationsList.sort(TwoTupleSort);

        return recommendationsList.subList(0, recommendationsList.size() > n ? n : recommendationsList.size());
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

        double sum_xy = 0.0d;
        double sum_x = 0.0d;
        double sum_y = 0.0d;
        double sum_x2 = 0.0d;
        double sum_y2 = 0.0d;
        double n = 0.0d;

        for (Map.Entry<String, String> entry : user1.entrySet()) {
            if (!user2.containsKey(entry.getKey())) {
                continue;
            }
            n += 1;
            double x = Double.parseDouble(entry.getValue());
            double y = Double.parseDouble(user2.get(entry.getKey()));

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
        double denominator = Math.sqrt(sum_x2 - Math.pow(sum_x, 2) / n)
                * Math.sqrt(sum_y2 - Math.pow(sum_y, 2) / n);
        if (denominator == 0) {
            return 0;
        }
        double r = (sum_xy - (sum_x * sum_y) / n) / denominator;
        return r;
    }

    public String trim(String value, char c) {
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();

        while ((st < len) && (val[st] <= c)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= c)) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }

    public HashMap<String, HashMap<String, String>> loadBookDB(String path) throws IOException {
        HashMap<String, HashMap<String, String>> hashMap = new HashMap<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path + "BX-Book-Ratings.txt"), Charset.forName("UTF-8"));) {
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = StringUtils.split(line, ';');
                String userId = this.trim(tokens[0], '"');
                String bookId = this.trim(tokens[1], '"');
                String rating = this.trim(tokens[2], '"');
                HashMap<String, String> map;
                if (hashMap.containsKey(userId)) {
                    map = hashMap.get(userId);
                    if (map.containsKey(bookId)) {
                        System.out.println(line);
                    }
                    map.put(bookId, rating);
                } else {
                    map = new HashMap<>();
                    map.put(bookId, rating);
                    hashMap.put(userId, map);
                }
                line = bufferedReader.readLine();

            }
        }
        System.out.println(hashMap.size());


        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path + "BX-Books.txt"), Charset.forName("UTF-8"));) {
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = StringUtils.split(line, ';');
                String isbn = this.trim(tokens[0], '"');
                String title = this.trim(tokens[1], '"');
                bookTitle.put(isbn, title);
                line = bufferedReader.readLine();

            }
        }
        return hashMap;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Recommender recommender = new Recommender(5, 6);
//        HashMap<String, HashMap<String, String>> mapHashMap = recommender.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/data.txt");
//        List<TwoTuple<String, Double>> recommend = recommender.recommend("Hailey", mapHashMap);
//        System.out.println(recommend);
        HashMap<String, HashMap<String, String>> hashMap = recommender.loadBookDB("D:/tmp/BX-CSV-Dump/");
        List<TwoTuple<String, Double>> recommend = recommender.recommend("171118", hashMap);
        System.out.println(recommend);
    }
}
