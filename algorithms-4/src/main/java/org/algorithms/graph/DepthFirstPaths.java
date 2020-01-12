package org.algorithms.graph;

import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 寻找路径
 */
public class DepthFirstPaths {
    private final int s;

    private final boolean[] marked;

    private final int[] edgeTo;

    public DepthFirstPaths(Graph graph, int s) {
        this.s = s;
        this.marked = new boolean[graph.V()];
        this.edgeTo = new int[graph.V()];
        dfs(graph, s);
    }

    private void dfs(Graph graph, int s) {
        marked[s] = true;
        for (int w : graph.adj(s)) {
            if (marked[w]) {
                continue;
            }
            //深度搜索记录顶点，先往下寻找，在加入顶点
            edgeTo[w] = s;//记录了一系列轨迹的,下标是子路径，值是父路径
            dfs(graph, w);
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        List<Integer> tmp = new ArrayList<>();
        //从叶子节点一直寻找到根节点
        for (int x = v; x != s; x = edgeTo[x]) {
            tmp.add(x);
        }
        tmp.add(s);
        Collections.reverse(tmp);
        return tmp;
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
        int s = 0;
        DepthFirstPaths paths = new DepthFirstPaths(graph, s);
        for (int v = 0; v < graph.V(); v++) {
            System.out.print(s + " to " + v + ": ");
            if (!paths.hasPathTo(v)) {
                System.out.println();
                continue;
            }
            for (int x : paths.pathTo(v)) {
                if (x == s) {
                    System.out.print(x);
                } else {
                    System.out.print("-" + x);
                }
            }
            System.out.println();
        }
    }
}
