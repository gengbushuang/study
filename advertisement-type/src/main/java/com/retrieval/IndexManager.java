package com.retrieval;

public class IndexManager {

//    private IndexProcessor indexProcessor;
//
//    private Store<Integer,Dnf> store;
//
//    public IndexManager(){
//        indexProcessor = new IndexProcessor();
//        store = new MemoryStore();
//    }
//
//    public void update(Dnf dnf){
//        boolean exist = store.isExist(dnf.getDocid());
//        if(exist){
//            this.delete(dnf);
//        }
//        this.add(dnf);
//    }
//
//    public void delete(Dnf dnf){
//        Dnf data = store.getData(dnf.getDocid());
//        indexProcessor.delete(data);
//        store.delete(dnf.getDocid(),dnf);
//    }
//
//    public void add(Dnf dnf){
//        store.add(dnf.getDocid(),dnf);
//        indexProcessor.add(dnf);
//    }
}
