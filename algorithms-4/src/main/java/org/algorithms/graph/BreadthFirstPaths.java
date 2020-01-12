package org.algorithms.graph;

import org.algorithms.data.struct.Queue;
import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BreadthFirstPaths {
    private final int s;

    private final boolean[] marked;

    private final int[] edgeTo;

    public BreadthFirstPaths(Graph graph, int s) {
        this.s = s;
        this.marked = new boolean[graph.V()];
        this.edgeTo = new int[graph.V()];
        bfs(graph, s);
    }

    private void bfs(Graph graph, int s) {
        marked[s] = true;
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        while (!queue.isEmpty()) {
            Integer v = queue.dequeue();
            for (int w : graph.adj(v.intValue())) {
                if (marked[w]) {
                    continue;
                }
                marked[w] = true;
                //广度搜索记录顶点，先加入顶点，在往下寻找
                edgeTo[w] = v.intValue();
                queue.enqueue(w);
            }
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
        BreadthFirstPaths paths = new BreadthFirstPaths(graph,s);
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
