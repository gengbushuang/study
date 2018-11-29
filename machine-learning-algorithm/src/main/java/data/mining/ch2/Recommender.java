package data.mining.ch2;

import org.apache.commons.lang3.StringUtils;
import org.nd4j.shade.jackson.core.JsonFactory;
import org.nd4j.shade.jackson.core.JsonParser;
import org.nd4j.shade.jackson.core.JsonToken;
import org.nd4j.shade.jackson.core.type.TypeReference;
import org.nd4j.shade.jackson.databind.JsonNode;
import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Recommender {

    public HashMap<String, HashMap<String, String>> load(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        TypeReference typeReference = new TypeReference<HashMap<String, HashMap<String, String>>>() {
        };

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, HashMap<String, String>> hashMap = mapper.readValue(bytes, typeReference);

        return hashMap;
    }

    public void recommend(String userName, HashMap<String, HashMap<String, String>> userMap) {
        computeNearestNeighbor(userName, userMap);
    }

    public void computeNearestNeighbor(String userName, HashMap<String, HashMap<String, String>> userMap) {

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
            float x = Float.parseFloat(entry.getValue());
            float y = Float.parseFloat(user2.get(entry.getKey()));

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
        return Math.round(r);
    }


    public static void main(String[] args) throws URISyntaxException, IOException {
        Recommender recommender = new Recommender();
        URL resource = Recommender.class.getResource("data.txt");
        System.out.println(resource);
        System.out.println(resource.toURI().getPath());
        HashMap<String, HashMap<String, String>> mapHashMap = recommender.load("D:/git/study/machine-learning-algorithm/target/classes/data/mining/ch2/data.txt");
        recommender.recommend("", mapHashMap);

    }
}
