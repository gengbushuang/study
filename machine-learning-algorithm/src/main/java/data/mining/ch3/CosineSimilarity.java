package data.mining.ch3;

import com.google.common.collect.Lists;
import org.nd4j.shade.jackson.core.type.TypeReference;
import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CosineSimilarity {

    /**
     * 计算平均值
     *
     * @param map
     */
    public float userAverages(Map<String, String> map) {
        if (map.isEmpty()) {
            return 0.0f;
        }
        Collection<String> values = map.values();
        int length = values.size();
        float sum = 0.0f;
        for (String v : values) {
            sum += Float.parseFloat(v);
        }
        return sum / length;
    }

    /**
     *
     */
    public double similarity(String bind1, String bind2, HashMap<String, HashMap<String, String>> map) {
        Map<String, Float> averages = new HashMap<>(map.size());
        Set<String> set_user = map.keySet();
        //计算每个用户对所有产品评价的均值
        for (String user : set_user) {
            Map<String, String> tmp = map.get(user);
            float f_avg = userAverages(tmp);
            averages.put(user, f_avg);
        }

        float v1_v2 = 0.0f;
        //这个商品对每个用户的评价减去均值评价
        double v1_sum = 0.0d;
        double v2_sum = 0.0d;
        set_user = map.keySet();
        for (String user : set_user) {
            HashMap<String, String> bind_map = map.get(user);
            if (!bind_map.containsKey(bind1) || !bind_map.containsKey(bind2)) {
                continue;
            }
            Float avg = averages.get(user);

            String u1 = bind_map.get(bind1);
            float v1 = Float.parseFloat(u1);
            //(v[当前用户对产品1评价]-avg[当前用户的评价均值])的平方进行累加
            v1_sum += Math.pow(v1 - avg.floatValue(), 2);

            String u2 = bind_map.get(bind2);
            float v2 = Float.parseFloat(u2);
            //(v[当前用户对产品2评价]-avg[当前用户的评价均值])的平方进行累加
            v2_sum += Math.pow(v2 - avg.floatValue(), 2);
            //(v[当前用户对产品1评价]-avg[当前用户的评价均值])*(v[当前用户对产品2评价]-avg[当前用户的评价均值])累加
            v1_v2 += (v1 - avg.floatValue()) * (v2 - avg.floatValue());
        }
        //对（v[当前用户对产品1评价进行开根号]*v[当前用户对产品2评价进行开根号]）
        double denominator = Math.sqrt(v1_sum) * Math.sqrt(v2_sum);
        return v1_v2 / denominator;


//        Map<String, String> bind1_map = map.get(bind1);
//        Map<String, String> bind2_map = map.get(bind2);
//        float v1_v2 = 0.0f;
//        double v1_sum = 0.0d;
//        double v2_sum = 0.0d;
//        Set<String> keys = bind1_map.keySet();
//        for (String k : keys) {
//            if (!bind2_map.containsKey(k)) {
//                continue;
//            }
//            String u1 = bind1_map.get(k);
//            float v1 = Float.parseFloat(u1);
//            v1_sum += Math.pow(v1 - averages.get(bind1), 2);
//
//            String u2 = bind2_map.get(k);
//            float v2 = Float.parseFloat(u2);
//            v2_sum += Math.pow(v2 - averages.get(bind2), 2);
//
//            v1_v2 += (v1 - averages.get(bind1)) * (v2 - averages.get(bind2));
//        }
//
//        double denominator = Math.sqrt(v1_sum) * Math.sqrt(v2_sum);
//
//        return v1_v2 / denominator;
    }

    public HashMap<String, HashMap<String, String>> load(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        TypeReference typeReference = new TypeReference<HashMap<String, HashMap<String, String>>>() {
        };

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, HashMap<String, String>> hashMap = mapper.readValue(bytes, typeReference);

        return hashMap;
    }

    public static void main(String[] args) throws IOException {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        HashMap<String, HashMap<String, String>> mapHashMap = cosineSimilarity.load("D:\\git\\study\\machine-learning-algorithm\\src\\main\\java\\data\\mining\\ch3\\data1.txt");

//        String key1 = "Lorde";
//        String key2 = "Daft Punk";
//        double cosine = cosineSimilarity.similarity(key1, key2, mapHashMap);
//        System.out.println("(" + key1 + "," + key2 + ")余玄相关系数->" + cosine);

        ArrayList<String> list = Lists.newArrayList("Imagine Dragons", "Daft Punk", "Lorde", "Fall Out Boy", "Kacey Musgraves");
        for (String key1 : list) {
            for (String key2 : list) {
                if (key1.equals(key2)) {
                    continue;
                }
                double cosine = cosineSimilarity.similarity(key1, key2, mapHashMap);
                System.out.println("(" + key1 + "," + key2 + ")余玄相关系数->" + cosine);
            }
        }



    }
}
