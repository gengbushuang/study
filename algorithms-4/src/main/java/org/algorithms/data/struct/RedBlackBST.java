package org.algorithms.data.struct;

/**
 * 红黑树
 *
 * @param <Key>
 * @param <Value>
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private final static boolean RED = true;
    private final static boolean BLACK = false;

    private Node root;

    private class Node {
        private Key k;

        private Value v;

        private Node left, right;

        private int n;

        private boolean color;

        Node(Key k, Value v, int n, boolean color) {
            this.k = k;
            this.v = v;
            this.n = n;
            this.color = color;
        }

    }

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : node.n;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void put(Key k,Value v){
        root = put(root,k,v);
        root.color = BLACK;
    }

    private Node put(Node node, Key k, Value v) {
        if (node == null) {
            return new Node(k, v, 1, RED);
        }
        int compare = k.compareTo(node.k);
        if (compare < 0) {
            node.left = put(node.left,k,v);
            //如果这个节点两个左子树都是红链接就对这个节点进行右旋转
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
        } else if (compare > 0) {
            node.right = put(node.right,k,v);
            //如果这个右子树链接为红链接，左子树链接为黑链接，进行左旋转
            if(isRed(node.right) && !isRed(node.left)){
                node = rotateLeft(node);
            }
        } else {
            node.v = v;
        }
        //如果这个节点的左右子树链接都为红链接，要把两个链接颜色变为黑色，节点变为红色
        if (isRed(node.left) && isRed(node.right)) {
            filpColors(node);
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void deleteMin() {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMin(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteMin(Node node){
        if(node.left==null){
            return null;
        }
        //判断左节点是否是2节点
        if(!isRed(node.left) && !isRed(node.left.left)){
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        //修复树
        return balance(node);
    }



    /**
     * 左旋转
     *         h                          x
     *        /\                         /\
     *      /   \                      /   \
     *    1      x  ---->             h     3
     *          /\                   /\
     *        /   \                /   \
     *       2     3              1     2
     * @param h
     * @return
     */
    Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;

        x.color = h.color;
        h.color = RED;

        x.n = h.n;
        h.n = size(h.left) + size(h.right) + 1;
        return x;
    }

    /**
     * 右旋转
     *         h                   x
     *        /\                  /\
     *      /   \               /   \
     *     x    1   <-----     2     h
     *    /\                        /\
     *  /   \                     /   \
     * 2     3                   3    1
     * @param h
     * @return
     */
    Node rotateRight(Node h){
        Node x = h.left;
        h.left = x.right;
        x.right = h;

        x.color = h.color;
        h.color = RED;

        x.n = h.n;
        h.n = size(h.left) + size(h.right) + 1;
        return x;
    }

    void filpColors(Node h) {
//        h.color = RED;
//        h.left.color = BLACK;
//        h.right.color = BLACK;
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node node) {
        //父节点和兄弟节点合并4-节点
        filpColors(node);
        //判断兄弟节点是否3-节点
        if (isRed(node.right.left)) {
            //
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            filpColors(node);
        }
        return node;
    }

    /**
     * 修复树平衡
     * @param node
     * @return
     */
    Node balance(Node node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            filpColors(node);
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public static void main(String[] args) {
        RedBlackBST<String,String> redBlackBST = new RedBlackBST<>();
        redBlackBST.put("S","s");
        redBlackBST.put("E","e");
        redBlackBST.put("A","a");
        redBlackBST.put("R","r");
        redBlackBST.put("C","c");
        redBlackBST.put("H","h");
        redBlackBST.put("X","x");
        redBlackBST.put("M","m");
        redBlackBST.put("P","p");
        redBlackBST.put("L","l");

        redBlackBST.deleteMin();
        redBlackBST.deleteMin();
    }
}
