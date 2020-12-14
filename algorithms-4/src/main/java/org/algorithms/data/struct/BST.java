package org.algorithms.data.struct;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BST<K extends Comparable<K>, V> {

    private Node root;

    private class Node {
        //键
        private K k;
        //值
        private V v;
        //子树的左节点
        private Node left;
        //子树的右节点
        private Node right;
        //节点大小
        private int n;

        public Node(K k, V v, int n) {
            this.k = k;
            this.v = v;
            this.n = n;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "k=" + k +
                    ", v=" + v +
                    '}';
        }
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node == null ? 0 : node.n;
    }

    public V get(K k) {
        return get(root, k);
    }

    private V get(Node node, K k) {
        if (node == null) {
            return null;
        }
        //比较大小
        int compare = k.compareTo(node.k);
        if (compare < 0) {
            return get(node.left, k);
        } else if (compare > 0) {
            return get(node.right, k);
        } else {
            return node.v;
        }
    }

    public void put(K k, V v) {
        root = put(root, k, v);
    }

    private Node put(Node node, K k, V v) {
        if (node == null) {
            return new Node(k, v, 1);
        }
        int compare = k.compareTo(node.k);
        if (compare < 0) {
            node.left = put(node.left, k, v);
        } else if (compare > 0) {
            node.right = put(node.right, k, v);
        } else {
            node.v = v;
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public V min() {
        if (root == null) {
            return null;
        }
        return min(root).v;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    public V max() {
        if (root == null) {
            return null;
        }
        return max(root).v;
    }

    private Node max(Node node) {
        if (node.right == null) {
            return node;
        }
        return max(node.right);
    }

    public V floor(K k) {
        Node node = floor(root, k);
        if (node == null) {
            return null;
        }
        return node.v;
    }

    //取小于等于
    private Node floor(Node node, K k) {
        if (node == null) {
            return null;
        }
        int compare = k.compareTo(node.k);
        if (compare < 0) {
            return floor(node.left, k);
        } else if (compare == 0) {
            return node;
        }
        Node t = floor(node.right, k);
        if (t == null) {
            return node;
        }
        return t;
    }

    public V ceiling(K k) {
        Node node = ceiling(root, k);
        if (node == null) {
            return null;
        }
        return node.v;
    }

    //取大于等于
    private Node ceiling(Node node, K k) {
        if (node == null) {
            return null;
        }
        int compare = k.compareTo(node.k);
        if (compare == 0) {
            return node;
        } else if (compare > 0) {
            return ceiling(node.right, k);
        }
        Node t = ceiling(node.left, k);
        if (t == null) {
            return node;
        }
        return t;
    }

    public K select(int n) {
        Node node = select(root, n);
        if (node == null) {
            return null;
        }
        return node.k;
    }

    private Node select(Node node, int n) {
        if (node == null) {
            return null;
        }
        //获取左子树节点树
        int t = size(node.left);
        //节点树大于n，就继续递归左子树
        if (t > n) {
            return select(node.left, n);
        } else if (t > n) {//节点树小于n，递归右子树，减去当前节点树
            return select(node.right, (n - t - 1));
        } else {//节点树相等就等于找到了
            return node;
        }
    }

    public void deleteMin(){
        deleteMin(root);
    }
        //s left !=null e
    //e left !=null a
    //a left ==null c
    //
    private Node deleteMin(Node node) {
        if(node.left==null){
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void delete(K k){
        delete(root,k);
    }

    private Node delete(Node node, K k) {
        if(node==null){
            return null;
        }
        int compare = k.compareTo(node.k);
        if (compare < 0) {
            node.left = delete(node.left, k);
        } else if (compare > 0) {
            node.right = delete(node.right, k);
        }else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node x = min(node.right);
                x.right = deleteMin(node.right);
                x.left = node.left;
                x.n = size(x.left) + size(x.right) + 1;
                return x;
            }
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void show(){
        show(root);
    }

    private void show(Node node){
        if(node==null)return;
        //前序遍历
        //System.out.println(node);
        show(node.left);
        //中序遍历
        //System.out.println(node);
        show(node.right);
        //后序遍历
        //System.out.println(node);
    }

    /**
     * 前序遍历首先访问根节点，然后遍历左子树，最后遍历右子树
     */
    public void showPreorderTraversal(){
        if(root==null){
            return;
        }
        Stack<Node> stack = new Stack<>(10);
        Node node = root;
        while (!stack.isEmpty() || node!=null){
            while (node!=null){
                System.out.println(node);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
//        stack.push(root);
//        while (!stack.isEmpty()){
//            Node node = stack.pop();
//            System.out.println(node);
//
//            if(node.right!=null){
//                stack.push(node.right);
//            }
//
//            if(node.left!=null){
//                stack.push(node.left);
//            }
//
//
//        }
    }

    /**
     * 中序遍历是先遍历左子树，然后访问根节点，然后遍历右子树。
     */
    public void showInorderTraversal(){
        if(root==null){
            return;
        }
        Stack<Node> stack = new Stack<>(10);
        Node node = root;
        while (!stack.isEmpty() || node!=null){
            while (node!=null){
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            System.out.println(node);
            node = node.right;
            
        }
    }

    /**
     * 后序遍历是先遍历左子树，然后遍历右子树，最后访问树的根节点。
     * //TODO 有时间优化
     */
    public void showPostorderTraversal(){
        if(root==null){
            return;
        }
        Stack<Node> stack = new Stack<>(10);
        Set<Node> set = new HashSet<>();
        Node node = root;
        Node lnode;
        while (!stack.isEmpty() || node!=null){
            while (node!=null){
                stack.push(node);
                node = node.left;
            }
            lnode = stack.pop();

            if(set.contains(lnode)){
                node = null;
            }else{
                node = lnode.right;
            }

            if(lnode.right!=null && !set.contains(lnode)){
                stack.push(lnode);
                set.add(lnode);
            }else{
                System.out.println(lnode);
            }
        }
    }


    public int maxDepth() {

        return maxDepth(root,0);
    }

    private int maxDepth(Node node, int depth) {
        if(node==null){
            return depth;
        }
        int maxLeft = maxDepth(node.left,depth+1);
        int maxRight =maxDepth(node.right,depth+1);
        return Math.max(maxLeft,maxRight);
    }


    public static void main(String[] args) {
        BST<String,String> bst = new BST<String,String>();
        bst.put("S","s");
        bst.put("E","e");
        bst.put("X","x");
        bst.put("A","a");
        bst.put("R","r");
        bst.put("C","c");
        bst.put("H","h");
        bst.put("M","m");
//        bst.put("Y","y");bst.put("Z","z");
//        bst.show();
        int i = bst.maxDepth();
        System.out.println(i);
        //bst.delete("E");
    }
}
