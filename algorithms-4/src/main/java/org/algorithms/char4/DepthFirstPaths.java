package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.char1.Stack;
import org.algorithms.util.Utils;
/**
 * 深度优先搜索
 * @author gbs
 *
 */
public class DepthFirstPaths {
	public boolean[] marked;
	private int[] engeTo;// 从一个起点到一个顶点已知路径上的最后一个路径
	private final int s;// 起点

	public DepthFirstPaths(Graph g, int s) {

		this.marked = new boolean[g.v()];
		this.engeTo = new int[g.v()];
		this.s = s;

		dfs(g, s);
	}
	
	private void dfs(Graph g, int v) {
		marked[v] = true;
		for (int w : g.adj(v)) {
			if (!marked[w]) {
				engeTo[w] = v;
				dfs(g, w);
			}
		}
	}

	public boolean hashPath(int v) {
		return marked[v];
	}

	public Iterable<Integer> pathTo(int v) {
		if (!hashPath(v)) {
			return null;
		}
		Stack<Integer> stack = new Stack<>();
		for (int x = v; x != s; x = engeTo[x]) {
			stack.push(x);
		}
		stack.push(s);
		return stack;
	}

	public static void main(String[] args) {
		Graph graph = null;
		try {
			Scanner scanner = Utils.getScanner("graph/tinyCG.txt");
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
		int s = 2;
		DepthFirstPaths search = new DepthFirstPaths(graph, s);
		for (int i = 0; i < graph.v(); i++) {
			System.out.print(s + " to " + i + ": ");
			if (search.hashPath(i)) {
				for (int x : search.pathTo(i)) {
					if (x == s) {
						System.out.print(x);
					} else {
						System.out.print("-" + x);
					}
				}
			}
			System.out.println();
		}
	}
}
