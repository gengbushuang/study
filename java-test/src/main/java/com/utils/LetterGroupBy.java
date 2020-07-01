package com.utils;

import java.util.*;

public class LetterGroupBy {

    int letter_offset = 97;

    public String[][] groupBy(String[] letters) {
        Map<Integer, List<String>> groupByMap = new TreeMap<>();
        for (String letter : letters) {
            char[] chars = letter.toCharArray();
            int toInt = this.charToInt(chars);
            List<String> putIfAbsent = groupByMap.computeIfAbsent(toInt, v -> new ArrayList<>());
            putIfAbsent.add(letter);
//            System.out.println(toInt + ",," + letter);
        }
//        System.out.println(groupByMap);
        return this.mapToArrays(groupByMap);

    }

    private String[][] mapToArrays(Map<Integer, List<String>> groupByMap) {
        if (groupByMap.isEmpty()) {
            return new String[0][0];
        }
        String[][] result = new String[groupByMap.size()][];
        Collection<List<String>> values = groupByMap.values();
        int index = 0;
        for (List<String> value : values) {
            result[index] = new String[value.size()];
            for (int i = 0; i < value.size(); i++) {
                result[index][i] = value.get(i);
            }
            index++;
        }
        return result;
    }

    private int charToInt(char[] chars) {
        int number = 0;
        for (char c : chars) {
            number |= (((int) c - letter_offset) << 1);
        }
        return number;
    }

    public static void main(String[] args) {
        String[] letters = {"eat", "tea", "tan", "ate", "nat", "bat"};
        String[][] strings = new LetterGroupBy().groupBy(letters);
        for (String s[] : strings) {
            System.out.println(Arrays.toString(s));
        }
    }
}
