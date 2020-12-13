package org.algorithms.graph.undirected.graph.struct;

import org.algorithms.data.struct.Bag;

/**
 * 无向图
 */
public class Graph {
    
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int v;

    private int e;

    private Bag<Integer>[] adj;

    public Graph(int v) {
        this.v = v;
        this.e = 0;
        this.adj = new Bag[v];
        for (int i = 0, len = adj.length; i < len; i++) {
            adj[i] = new Bag<>();
        }
    }

    /**
     * 顶点数
     *
     * @return
     */
    public int v() {
        return v;
    }

    /**
     * 边数
     *
     * @return
     */
    public int e() {
        return e;
    }

    /**
     * 向图中添加v到w的边
     *
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        this.adj[v].add(w);
        this.adj[w].add(v);
        this.e++;
    }

    public Iterable<Integer> adj(int v) {
        return this.adj[v];
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(v + " vertices, " + e + " edges " + NEWLINE);
        for (int i = 0; i < v; i++) {
            s.append(i + ": ");
            for (int w : adj[i]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
