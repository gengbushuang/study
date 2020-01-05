package org.algorithms.graph;

import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 深度优先搜索
 */
public class DepthFirstSearch {

    private final boolean[] marked;

    private int count;

    public DepthFirstSearch(Graph g, int s) {
        this.marked = new boolean[g.V()];
        this.count = 0;
        dfs(g, s);
    }

    private void dfs(Graph g, int s) {
        marked[s] = true;
        count++;
        for (int v : g.adj(s)) {
            if (marked[v]) {
                continue;
            }
            dfs(g, v);
        }
    }

    public boolean marked(int v) {
        return marked[v];
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

        DepthFirstSearch search = new DepthFirstSearch(graph, 0);
        for (int v = 0; v < graph.V(); v++) {
            if (search.marked(v)) {
                System.out.print(v + " ");
            }
        }
    }
}
