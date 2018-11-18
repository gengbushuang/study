package data.mining.ch2;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class FilteringdataPearson {


    public static void main(String[] args) {
//        Map<String, String[]> map = new HashMap<>();
//        map.put("Clara", new String[]{"4.75", "4.5", "5", "4.25", "4"});
//        map.put("Robert", new String[]{"4", "3", "5", "2", "1"});
//        double pearson = new FilteringdataPearson().pearson("Clara", "Robert", map);
//        System.out.println(pearson);

        Map<String, String[]> map = new HashMap<>();
        map.put("Jake", new String[]{"0","0","0","4.5", "5", "4.5", "0", "0","0","0"});
        map.put("Cooper", new String[]{"0","0","4","5", "5", "5", "0", "0","0","0"});
        map.put("Kelsey", new String[]{"5","4","4","5", "5", "5", "5", "5","4","4"});
        double cosine = new FilteringdataPearson().cosine("Cooper","Jake",map);
        System.out.println(cosine);
    }

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
        return Math.round(r);
    }

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
}
