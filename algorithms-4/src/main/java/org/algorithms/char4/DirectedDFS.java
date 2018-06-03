package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.algorithms.util.Utils;
/**
 * 有向图深度搜索
 * @author gbs
 *
 */
public class DirectedDFS {

	private boolean[] marked;

	public DirectedDFS(Digraph g, int s) {
		this.marked = new boolean[g.v()];
		dfs(g, s);
	}

	public DirectedDFS(Digraph g, Iterable<Integer> sources) {
		this.marked = new boolean[g.v()];
		for (int s : sources) {
			if (!marked[s]) {
				dfs(g, s);
			}
		}
	}

	private void dfs(Digraph g, int s) {
		marked[s] = true;
		for (int v : g.adj(s)) {
			if (!marked[v]) {
				dfs(g, v);
			}
		}
	}

	public boolean marked(int v) {
		return marked[v];
	}

	public static void main(String[] args) {
		Digraph digraph;
		try {
			Scanner scanner = Utils.getScanner("graph/tinyDG.txt");
			digraph = new Digraph(scanner.nextInt());
			int e = scanner.nextInt();
			for (int i = 0; i < e; i++) {
				int v = scanner.nextInt();
				int w = scanner.nextInt();
				digraph.addEdge(v, w);
				// System.out.println(v + "," + w);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		List<Integer> sources = Stream.of(6).collect(Collectors.toList());

		DirectedDFS directedDFS = new DirectedDFS(digraph, sources);
		for (int v = 0; v < digraph.v(); v++) {
			if (directedDFS.marked(v)) {
				System.out.print(v + " ");
			}
		}
		System.out.println();
	}
}
