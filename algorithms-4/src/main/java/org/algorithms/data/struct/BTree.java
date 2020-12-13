package org.algorithms.data.struct;

/**
 * B树
 * 所有叶子节点到根节点的路径长度相同，即具有相同的高度；
 * 每个非叶子和非根节点（即内部节点）至少有t-1个孩子节点；根至少2个孩子
 * 每个节点最多有2t个孩子节点
 * 每个节点的孩子比key的个数多1
 */
public class BTree<Key extends Comparable<Key>, Value> {

    private static final int M = 4;
    //树高度
    private int height;

    private Node root;

    private static final class Node {
        //节点已经有多少个key
        private int m;

        private Entry[] children = new Entry[M];

        private Node(int m) {
            this.m = m;
        }

    }

    private static final class Entry {
        private Comparable key;
        private Object val;
        private Node next;

        private Entry(Comparable key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public BTree() {
        root = new Node(0);
    }

    public Value get(Key k) {
        return search(root, k, height);
    }

    private Value search(Node r, Key k, int height) {
        Entry[] children = r.children;
        if (height == 0) {
            for (int i = 0; i < r.m; i++) {
                if (eq(k, children[i].key)) {
                    return (Value) children[i].val;
                }
            }
        } else {
            for (int i = 0; i < r.m; i++) {
                if (i + 1 == r.m || less(k, children[i + 1].key)) {
                    return search(r.children[i].next, k, height - 1);
                }
            }
        }
        return null;
    }

    public void put(Key k, Value v) {
        Node u = insert(root, k, v, height);

        if (u == null) {
            return;
        }
        Node t = new Node(2);
        t.children[0] = new Entry(-1, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node r, Key k, Value v, int height) {
        Entry t = new Entry(k, v, null);
        int j;
        //树高度为0表示当前是叶子节点
        if (height == 0) {
            for (j = 0; j < r.m; j++) {
                //判断
                if (less(k, r.children[j].key)) {
                    break;
                }
            }
        } else {
            for (j = 0; j < r.m; j++) {
                if (j + 1 == r.m || less(k, r.children[j + 1].key)) {
                    Node u = insert(r.children[j++].next, k, v, height - 1);
                    if (u == null) {
                        return null;
                    }
                    t.key = u.children[0].key;
                    t.val = null;
                    t.next = u;
                    break;
                }
            }
        }
        //对节点里面数组后移
        for (int i = r.m; i > j; i--) {
            r.children[i] = r.children[i - 1];
        }
        r.children[j] = t;
        r.m++;
        //判断节点多少个数据要进行分裂
        if (r.m < M) {
            return null;
        } else {
            //进行分裂
            return split(r);
        }
    }

    private Node split(Node r) {
        Node t = new Node(M / 2);
        r.m = M / 2;
        for (int j = 0; j < M / 2; j++) {
            t.children[j] = r.children[M / 2 + j];
            r.children[M / 2 + j] = null;
        }
        return t;
    }


    /**
     * k1是否小于k2
     *
     * @param k1
     * @param k2
     * @return
     */
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree<Integer, String>();

        int[] ints = new int[]{6, 10, 4, 14, 5, 11, 15, 3, 2, 12, 1, 7, 8, 8, 6, 3, 6, 21, 5, 15, 15, 6, 32, 23, 45, 65, 7, 8, 6, 5, 4};

        for (int i = 0; i < ints.length; i++) {
            tree.put(ints[i], String.valueOf(ints[i]));
        }

        String s = tree.get(65);
        System.out.println(s);
    }
}
