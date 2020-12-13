package com.wand;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class WAND {


    private Map<String, Double> term_ub = new HashMap<>();

    private Map<String, ConcurrentSkipListSet<Integer>> term_index = new HashMap<>();

    private HeapPQ<Document> heapPQ = new HeapPQ<>(20);

    private List<List<Integer>> posting = new ArrayList<>();

    public void add(List<Doc> docs) {

        addDocs(docs);

        for (Doc doc : docs) {
            update_term_ub(doc);
        }
    }

    private void addDocs(List<Doc> docs) {
        for (Doc doc : docs) {
            String[] keyword = doc.getKeyword();
            for (String kw : keyword) {
                ConcurrentSkipListSet listSet = term_index.get(kw);
                if(listSet==null){
                    listSet = new ConcurrentSkipListSet<>();
                    term_index.put(kw,listSet);
                }
                listSet.add(doc.getDocId());
            }
        }
    }

    private void update_term_ub(Doc doc) {
        String[] keyword = doc.getKeyword();
        for (String kw : keyword) {
            double ub = (double) 1 / (double) keyword.length;
            Double tmp = term_ub.get(kw);
            if (tmp == null) {
                tmp = new Double(ub);
            } else {
                tmp = new Double(tmp.doubleValue() + ub);
            }
            term_ub.put(kw, tmp);
        }
    }

    public void retrieve(List<String> querys) {

        int curDoc = 0;
        double minScore = 0;

        for (int i = 0; i < querys.size(); i++) {
            ConcurrentSkipListSet<Integer> skipListSet = term_index.get(querys.get(i));
            Integer[] integers = skipListSet.toArray(new Integer[0]);
            List<Integer> tmp = new ArrayList<>();
            Collections.addAll(tmp,integers);
            posting.add(tmp);
        }
    }

    public static void main(String[] args) {
        List<Doc> docList = new ArrayList<>();
        docList.add(new Doc(1, new String[]{"I", "love", "you"}));
        docList.add(new Doc(2, new String[]{"I", "hate", "you"}));
        docList.add(new Doc(3, new String[]{"I", "miss", "you"}));

        WAND wand = new WAND();
        wand.add(docList);

    }
}
