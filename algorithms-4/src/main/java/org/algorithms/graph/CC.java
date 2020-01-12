package org.algorithms.graph;

import org.algorithms.data.struct.Bag;
import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 连通性
 */
public class CC {


    int count = 0;
    private int[] ids;
    private final boolean[] marked;

    public CC(Graph graph) {
        this.ids = new int[graph.V()];
        this.marked = new boolean[graph.V()];
        for (int v = 0; v < graph.V(); v++) {
            if (marked[v]) {
                continue;
            }
            dfs(graph, v);
            count++;
        }
    }

    private void dfs(Graph graph, int s) {
        marked[s] = true;
        ids[s] = count;
        for (int w : graph.adj(s)) {
            if (marked[w]) {
                continue;
            }
            dfs(graph, w);
        }
    }

    public boolean connected(int v, int w) {
        return ids[v] == ids[w];
    }

    public int count() {
        return count;
    }

    public int id(int v) {
        return ids[v];
    }

    public static void main(String[] args) {
        Graph graph;
        try {
            Scanner scanner = Utils.getScanner("graph/tinyG.txt");
            int V = scanner.nextInt();
            graph = new Graph(V);
            int E = scanner.nextInt();
            for (int i = 0; i < E; i++) {
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.addEdge(v, w);
            }
            scanner.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
        }

        CC cc = new CC(graph);

        int M = cc.count();
        System.out.println(M + " components");

        Bag<Integer>[] components = new Bag[M];
        for (int i = 0; i < M; i++) {
            components[i] = new Bag<>();
        }

        for (int v = 0; v < graph.V(); v++) {
            components[cc.id(v)].add(v);
        }

        for (int i = 0; i < M; i++) {
            for (int v : components[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }
}
