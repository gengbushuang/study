package org.algorithms.graph.undirected.graph;

import org.algorithms.graph.undirected.graph.struct.Graph;
import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 深度优先搜索
 */
public class DepthFirstSearch {

    private boolean[] mark;
    private int count = 0;

    public DepthFirstSearch(Graph g, int s) {
        this.mark = new boolean[g.v()];
        loop(g, s);
    }

    private void recursion(Graph g, int v) {
        mark[v] = true;
        count++;
        for (Integer w : g.adj(v)) {
            if (!mark[w]) {
                recursion(g, w);
            }
        }
    }

    private void loop(Graph g, int v) {
        mark[v] = true;
        String s_loop = String.valueOf(v);
        while (!s_loop.equals("")) {
            String[] split_loop = s_loop.split("-");
            s_loop = "";
            for (String s_v : split_loop) {
                count++;
                for (Integer w : g.adj(Integer.parseInt(s_v))) {
                    if (!mark[w]) {
                        mark[w] = true;
                        s_loop = s_loop.equals("") ? w.toString() : s_loop + "-" + w.toString();
                    }
                }
            }
        }
    }

    /**
     * v和s是否连通
     *
     * @param v
     * @return
     */
    public boolean marked(int v) {
        return mark[v];
    }

    /**
     * 与s连通的顶点总数
     *
     * @return
     */
    public int count() {
        return count;
    }

    public static void main(String[] args) {
        Graph graph;
        try {
            Scanner scanner = Utils.getScanner("graph/tinyG.txt");
            graph = new Graph(scanner.nextInt());
            int e = scanner.nextInt();
            for (int i = 0; i < e; i++) {
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.addEdge(v, w);
                // System.out.println(v + "," + w);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        DepthFirstSearch search = new DepthFirstSearch(graph, 9);
        for (int v = 0; v < graph.v(); v++) {
            if (search.marked(v)) {
                System.out.print(v + " ");
            }
        }
        System.out.println();

        if (search.count() != graph.v()) {
            System.out.print("NOT ");
        }
        System.out.println("connected");
    }

}
