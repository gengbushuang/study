package com.retrieval.data;

import com.retrieval.model.Dnf;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MemoryStore {

    private ConcurrentSkipListMap<Integer, Dnf> table = new ConcurrentSkipListMap<>();



    public void update(){
    }

    public void add(){

    }

    public void delete(){

    }

}
