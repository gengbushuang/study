package org.algorithms.java.search;

public class SequentialSearchST<K, V> {

    private int n;
    private Node first;

    private class Node {
        private K k;
        private V v;
        private Node next;

        Node(K k, V v, Node n) {
            this.k = k;
            this.v = v;
            this.next = n;
        }
    }


    public V get(K k) {
        if (k == null) {
            //抛出异常
        }
        for (Node node = first; node != null; node = node.next) {
            if (k.equals(node.k)) {
                return node.v;
            }
        }
        return null;
    }

    public void put(K k, V v) {
        if (k == null) {
            //抛出异常
        }
        if (v == null) {
            delete(k);
            return;
        }
        for (Node node = first; node != null; node = node.next) {
            if (k.equals(node.k)) {
                node.v = v;
                return;
            }
        }
        first = new Node(k, v, first);
        n++;
    }

    public void delete(K k) {
        Node pnode = null;
        Node node = first;
        while (node != null) {
            if (k.equals(node.k)) {
                if (pnode == null) {
                    first = node.next;
                } else {
                    pnode.next = node.next;
                }
                n--;
                return;
            }
            pnode = node;
            node = node.next;
        }
    }

    public static void main(String[] args) {
        SequentialSearchST<String, String> st = new SequentialSearchST<>();
        st.put("a", "a");
        st.put("b", "b");
        st.put("c", "c");
        st.put("d", "d");
        st.put("e", "e");

        st.delete("b");
    }
}
