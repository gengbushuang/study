package org.algorithms.graph;

import org.algorithms.util.Utils;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class Search {
    private final Graph graph;
    private final int s;

    public Search(Graph g, int s) {
        this.graph = g;
        this.s = s;
    }

    public boolean marked(int v) {
        boolean [] mark = new boolean[graph.V()];
        return marked(v,mark);
    }

    private boolean marked(int v,boolean [] mark){
        for (Integer i : graph.adj(v)) {
            if(mark[i.intValue()]){
                continue;
            }
            if (i.intValue() == s) {
                return true;
            }
            mark[i.intValue()] = true;
            return marked(i,mark);
        }
        return false;
    }

    public int count() {
        int count = 0;
        for (Integer i : graph.adj(s)) {
            count++;
        }
        return count;
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

        Search search = new Search(graph, 9);
        for (int v = 0; v < graph.V(); v++) {
            if(search.marked(v)){
                System.out.print(v+" ");
            }
        }
    }
}
