package org.algorithms.graph;


import org.algorithms.data.struct.Bag;
import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.*;

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

    public int V() {
        return this.v;
    }

    public int E() {
        return this.e;
    }

    public void addEdge(int v, int w) {
        this.adj[v].add(w);
        this.adj[w].add(v);
        this.e++;
    }

    public Iterable<Integer> adj(int v) {
        return this.adj[v];
    }

    public String toString(){
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

    public static void main(String[] args) {
        Graph graph;
        int V = 0;
        try {
            Scanner scanner = Utils.getScanner("graph/tinyG.txt");
            V = scanner.nextInt();
            graph = new Graph(V);
            int E = scanner.nextInt();
//            System.out.println(E);
            for (int i = 0; i < E; i++) {
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.addEdge(v, w);
//                System.out.println(v + "," + w);
            }
            scanner.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
        }
        System.out.println(graph);
    }
}
