package org.algorithms.graph.undirected.graph;


import org.algorithms.graph.undirected.graph.struct.Graph;
import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 找到和起点s连通的所有顶点
 */
public class Search {
    final Graph g;
    final int s;

    public Search(Graph g, int s) {
        this.g = g;
        this.s = s;
    }

    /**
     * v和s是否连通
     *
     * @param v
     * @return
     */
    public boolean marked(int v) {
        boolean[] mark = new boolean[g.v()];
        return loop(mark, v);
    }

    /**
     * 递归查询
     *
     * @param mark
     * @param v
     * @return
     */
    private boolean recursion(boolean[] mark, int v) {
        for (Integer w : g.adj(v)) {
            if (mark[w.intValue()]) {
                continue;
            }
            if (w.intValue() == s) {
                return true;
            }
            mark[w.intValue()] = true;
            return recursion(mark, w);
        }
        return false;
    }

    /**
     * 循环查询
     *
     * @param mark
     * @param v
     * @return
     */
    private boolean loop(boolean[] mark, int v) {
        String s_loop = String.valueOf(v);
        while (!s_loop.equals("")) {
            String[] split_loop = s_loop.split("-");
            s_loop = "";
            for (String s_v : split_loop) {
                for (Integer w : g.adj(Integer.parseInt(s_v))) {
                    if (mark[w.intValue()]) {
                        continue;
                    }
                    if (w.intValue() == s) {
                        return true;
                    }
                    mark[w.intValue()] = true;
                    s_loop = s_loop.equals("") ? w.toString() : s_loop + "-" + w.toString();
                }
            }
        }
        return false;
    }

    /**
     * 与s连通的顶点总数
     *
     * @return
     */
    public int count() {
        int num = 0;
        for (Integer w : g.adj(s)) {
            num++;
        }
        return num;
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
        Search search = new Search(graph, 0);
        for (int v = 0; v < graph.v(); v++) {
            if (search.marked(v)) {
                System.out.print(v + " ");
            }
        }
        System.out.println();

    }
}
