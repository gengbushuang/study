package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.char1.Queue;
import org.algorithms.char1.Stack;
import org.algorithms.util.Utils;

/**
 * 广度优先搜索
 * 
 * @author gbs
 *
 */
public class BreadthFirstPaths {
	private boolean[] marked;// 到达该顶点的最短路径已知吗
	private int[] edgeTo;// 到达该顶点的已知路径上的最后一个顶点
	private final int s;

	public BreadthFirstPaths(Graph g, int s) {
		marked = new boolean[g.v()];
		edgeTo = new int[g.v()];
		this.s = s;
		bfs(g, s);
	}

	private void bfs(Graph g, int s) {
		marked[s] = true;
		Queue<Integer> queue = new Queue<>();
		queue.enqueue(s);
		while (!queue.isEmpty()) {
			int v = queue.dequeue();
			for (int w : g.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v; // 保存最短路径的最后一条边
					queue.enqueue(w);// 添加到队列中
					marked[w] = true;// 标记
				}
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
		Stack<Integer> stack = new Stack<>();
		for (int x = v; x != s; x = edgeTo[x]) {
			stack.push(x);
		}
		stack.push(s);
		return stack;
	}

	public static void main(String[] args) {
		Graph graph = null;
		try {
			Scanner scanner = Utils.getScanner("tinyCG.txt");
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
		int s = 4;
		BreadthFirstPaths paths = new BreadthFirstPaths(graph, s);
		for (int i = 0; i < graph.v(); i++) {
			System.out.print(s + " to " + i + ": ");
			if (paths.hasPathTo(i)) {
				for (int x : paths.pathTo(i)) {
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
