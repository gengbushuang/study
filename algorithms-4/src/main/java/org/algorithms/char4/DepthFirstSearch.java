package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.util.Utils;

/**
 * 深度优先搜索
 * @author gbs
 *
 */
public class DepthFirstSearch {

	public boolean[] marked;
	private int count;

	public DepthFirstSearch(Graph g, int s) {

		this.marked = new boolean[g.v()];
		dfs(g, s);
	}

	private void dfs(Graph g, int v) {
		marked[v] = true;
		count++;
		for (int w : g.adj(v)) {
			if (!marked(w)) {
				dfs(g, w);
			}
		}
	}

	public boolean marked(int w) {
		return marked[w];
	}

	public int count() {
		return count;
	}

	public static void main(String[] args) {
		Graph graph = null;
		try {
			Scanner scanner = Utils.getScanner("tinyG.txt");
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
		}
		
		DepthFirstSearch search = new DepthFirstSearch(graph, 0);
		
		for(int i = 0;i<graph.v();i++) {
			if(search.marked(i)) {
				System.out.print(i+" ");
			}
		}
		System.out.println();
		if(search.count!=graph.v()) {
			System.out.print("NOT");
		}
		System.out.println("connected");
	}
}
