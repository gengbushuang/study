package com.utils;

import java.util.*;

public class FrequencyCount {

    private final Comparator<Map.Entry<String, Integer>> wordSort = new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    };

    public String[] topk(String[] words, int k) {
        Map<String, Integer> countWord = this.count(words);
        int size = k > countWord.size() ? countWord.size() : k;
        return resultTopk(countWord, size);
    }

    private String[] resultTopk(Map<String, Integer> countWord, int size) {
        String[] result = new String[size];
        List<Map.Entry<String, Integer>> wordList = new ArrayList<Map.Entry<String, Integer>>(countWord.entrySet());
        wordList.sort(wordSort);
        for (int i = 0; i < size; i++) {
            result[i] = wordList.get(i).getKey();
        }

        return result;
    }

    private Map<String, Integer> count(String[] words) {
        Map<String, Integer> countWord = new LinkedHashMap<>();
        for (String word : words) {
            Integer count = countWord.get(word);
            if (count == null) {
                count = new Integer(1);
            } else {
                count = new Integer(count.intValue() + 1);
            }
            countWord.put(word, count);
        }

        return countWord;
    }

    public static void main(String[] args) {
        String[] words = {"i", "love", "leetcode", "i", "love", "coding"};
        int k = 2;

        words = new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
        k = 4;
        String[] topk = new FrequencyCount().topk(words, k);
        System.out.println(Arrays.toString(topk));
    }
}
