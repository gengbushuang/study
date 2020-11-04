package com.retrieval.test;

import com.retrieval.indexer.model.token.TokenRange;

public class IntervalTree {

    private final static boolean RED = true;
    private final static boolean BLACK = false;

    IntervalNode root = null;

    class IntervalNode{
        TokenRange token;
        long max;
        IntervalNode left;
        IntervalNode right;

        private boolean color;
        public IntervalNode(TokenRange token, boolean color){
            this.token = token;
            this.max = token.getRight();
            this.left = this.right = null;

            this.color = color;
        }

        @Override
        public String toString() {
            return "IntervalNode{" +
                    "token=" + token +
                    ",max="+max+
                    ",color="+(color?"RED":"BLACK")+"}";
        }
    }

    public void add(TokenRange token){
        root = add(root,token);
        root.color = BLACK;
    }

    private IntervalNode add(IntervalNode node, TokenRange token){
        if(node == null){
            return new IntervalNode(token,RED);
        }
        long left = node.token.getLeft();

        if(token.getLeft()<left){
            node.left = add(node.left,token);
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
        }else{
            node.right = add(node.right,token);
            if(isRed(node.right) && !isRed(node.left)){
                node = rotateLeft(node);
            }
        }

        if (isRed(node.left) && isRed(node.right)) {
            filpColors(node);
        }

        if(node.max<token.getRight()){
            node.max = token.getRight();
        }

        return node;
    }

    public void search(long point){
        IntervalNode intervalNode = search(root, point);
        System.out.println(intervalNode);
    }


    public IntervalNode intervalSearch(TokenRange range){
        return intervalSearch(root, range);
    }

    public IntervalNode intervalSearch(IntervalNode node, TokenRange range) {
        while (node!=null && !overlap(node.token,range)){
            if(node.left!=null &&node.left.max>=range.getLeft()){
                node = node.left;
            }else{
                node = node.right;
            }
        }
        return node;
    }

    public void show(){
        this.show(root);
    }

    private void show(IntervalNode node){
        if(node==null){
            return;
        }
        this.show(node.left);
        System.out.println(node);
        this.show(node.right);
    }

    private boolean overlap(TokenRange node,TokenRange range){
        if(node.getLeft()<=range.getRight() && node.getRight()>=range.getLeft() ){
            return true;
        }
        return false;
    }


    private IntervalNode search(IntervalNode node,long point){
        if(node==null){
            return null;
        }

        if (node.token.getLeft() <=point && point <= node.token.getRight()){
            return node;
        }

        if(node.left!=null &&node.left.max>=point){
            return search(node.left,point);
        }
       return search(node.right,point);
    }

    IntervalNode rotateLeft(IntervalNode h) {
        IntervalNode x = h.right;
        h.right = x.left;
        x.left = h;

        x.color = h.color;
        h.color = RED;

        return x;
    }

    IntervalNode rotateRight(IntervalNode h){
        IntervalNode x = h.left;
        h.left = x.right;
        x.right = h;

        x.color = h.color;
        h.color = RED;
        return x;
    }

    void filpColors(IntervalNode h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private boolean isRed(IntervalNode node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
    }

    public static void main(String[] args) {
        TokenRange r1 = new TokenRange("range",15,20);
        TokenRange r2 = new TokenRange("range",10,30);
        TokenRange r3 = new TokenRange("range",17,19);
        TokenRange r4 = new TokenRange("range",5,20);
        TokenRange r5 = new TokenRange("range",12,15);
        TokenRange r6 = new TokenRange("range",30,40);

        IntervalTree rangeTree = new IntervalTree();
        rangeTree.add(r1);
        rangeTree.add(r2);
        rangeTree.add(r3);
        rangeTree.add(r4);
        rangeTree.add(r5);
        rangeTree.add(r6);
        rangeTree.show();

        rangeTree.intervalSearch(new TokenRange("range",6,7));

//        TokenRange r1 = new TokenRange("range",16,21);
//        TokenRange r2 = new TokenRange("range",8,9);
//        TokenRange r3 = new TokenRange("range",25,30);
//        TokenRange r4 = new TokenRange("range",5,8);
//        TokenRange r5 = new TokenRange("range",15,23);
//        TokenRange r6 = new TokenRange("range",17,19);
//        TokenRange r7 = new TokenRange("range",26,26);
//        TokenRange r8 = new TokenRange("range",0,3);
//        TokenRange r9 = new TokenRange("range",6,10);
//        TokenRange r10 = new TokenRange("range",19,20);
//
//        IntervalTree rangeTree = new IntervalTree();

//        rangeTree.add(r1);
//        rangeTree.add(r2);
//        rangeTree.add(r3);
//        rangeTree.add(r4);
//        rangeTree.add(r5);
//        rangeTree.add(r6);
//        rangeTree.add(r7);
//        rangeTree.add(r8);
//        rangeTree.add(r9);
//        rangeTree.add(r10);
//        rangeTree.show();

//        TokenRange r11 = new TokenRange("range",21,21);
//
//
//        IntervalNode intervalNode = rangeTree.intervalSearch(r11);
//        while (intervalNode!=null){
//            System.out.println(intervalNode);
//        }



//        while (intervalNode!=null){
//
//        }
    }
}
