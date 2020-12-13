package com.utils;

import java.util.concurrent.ThreadLocalRandom;

public class SkipList<K extends Comparable<K>> {

    private int skipInterval;

    private int skipMultiplier;

    private int size = 0;

    static final int MAX_LEVEL = 4;

    final Node<K> head = new Node<>(null, MAX_LEVEL);

    private static final class Node<K extends Comparable<K>> {

        final K item;
        final Node<K>[] next;

        private int level;

        public Node(K k, int level) {
            this.item = k;

            this.next = new Node[level + 1];

            this.level = level;
        }

    }

    public SkipList(int skipInterval, int skipMultiplier) {
        this.skipInterval = skipInterval;
        this.skipMultiplier = skipMultiplier;
    }

    private Node search(K k, Node<K>[] precursors) {
        Node<K> precursor = head;
        Node<K> tmp = null;
        for (int level = MAX_LEVEL; level >= 0; level--) {
            Node<K> current = precursor.next[level];
            while (current != null && k.compareTo(current.item) > 0) {
                precursor = current;
                current = precursor.next[level];
            }
            if (current != null && k.compareTo(current.item) == 0) {
                tmp = current;
            }
            precursors[level] = precursor;
        }

        return tmp;
    }


    public void add(K k) {
        Node<K>[] precursors = new Node[MAX_LEVEL + 1];

        Node node = search(k, precursors);
        if (node != null) {
            return;
        }
        size++;
        int topLevel = randomLevel(size);

        Node<K> newNode = new Node<>(k, topLevel);
        for (int level = 0; level <= topLevel; level++) {
            newNode.next[level] = precursors[level].next[level];
            precursors[level].next[level] = newNode;
        }


    }

    public boolean remove(K k) {
        Node<K>[] precursors = new Node[MAX_LEVEL + 1];
        Node node = search(k, precursors);
        if (node == null) {
            return false;
        }
        int topLevel = node.level;

        for (int level = 0; level <= topLevel; level++) {
            Node<K> kNode = precursors[level].next[level];
            precursors[level].next[level] = kNode.next[level];
        }
        return true;
    }

    private int randomLevel(int size) {
//        if ((size % skipInterval) != 0) {
//            return 0;
//        }
//        int levelSize = size;
//        int level = 1;
//        levelSize /= skipInterval;
//
//        while ((levelSize % skipMultiplier) == 0 && level < MAX_LEVEL) {
//            level++;
//            levelSize /= skipMultiplier;
//        }
        int level = 0;
        while (ThreadLocalRandom.current().nextInt(2) != 0) {
            level++;
        }

        level = (MAX_LEVEL > level) ? level : MAX_LEVEL;

        return level;
    }

    public static void main(String[] args) {
        SkipList<Integer> skipList = new SkipList<>(3, 3);
        skipList.add(10);
        skipList.add(20);
        skipList.add(30);
        skipList.add(15);
        skipList.add(21);
        skipList.add(5);


//        skipList.remove(15);
//
        for (int i = 22; i < 30; i++) {
            skipList.add(i);
        }

    }
}
