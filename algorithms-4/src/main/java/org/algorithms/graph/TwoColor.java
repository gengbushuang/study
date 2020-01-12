package org.algorithms.graph;

import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 判断是否二分图
 */
public class TwoColor {

    private final boolean[] marked;
    private final boolean[] color;
    private boolean isTwoColor = true;

    public TwoColor(Graph graph) {
        this.marked = new boolean[graph.V()];
        this.color = new boolean[graph.V()];
        for (int v = 0; v < graph.V(); v++) {
            if (marked[v]) {
                continue;
            }
            dfs(graph, v);
        }
    }

    private void dfs(Graph graph, int s) {
        marked[s] = true;
        for (int w : graph.adj(s)) {
            if (!marked[w]) {
                color[w] = !color[s];
                dfs(graph, w);
            } else if (color[w] == color[s]) {
                isTwoColor = false;
            }
        }
    }

    public boolean isBipartite() {
        return isTwoColor;
    }

    public static void main(String[] args) {
        Graph graph;
        try {
            Scanner scanner = Utils.getScanner("graph/text.txt");
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

        TwoColor twoColor = new TwoColor(graph);
        System.out.println(twoColor.isBipartite());
    }
}
