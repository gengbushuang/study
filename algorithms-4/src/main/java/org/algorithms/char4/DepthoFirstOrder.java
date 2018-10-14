package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.char1.Queue;
import org.algorithms.char1.Stack;
import org.algorithms.util.Utils;

public class DepthoFirstOrder {
	private boolean[] marked;
	private Queue<Integer> pre; // 所有顶点的前序排列
	private Queue<Integer> post; // 所有顶点的后序排列
	private Stack<Integer> reversePost; // 所有顶点的逆后序排列

	public DepthoFirstOrder(Digraph g) {
		pre = new Queue<Integer>();
		post = new Queue<Integer>();
		reversePost = new Stack<Integer>();
		marked = new boolean[g.v()];
		for (int v = 0; v < g.v(); v++) {
			if (!marked[v]) {
				dfs(g, v);
			}
		}
	}

	private void dfs(Digraph g, int v) {
		marked[v] = true;
		pre.enqueue(v);
		for (int w : g.adj(v)) {
			if (!marked[w]) {
				dfs(g, w);
			}
		}
		post.enqueue(v);
		reversePost.push(v);
	}

	public Iterable<Integer> pre() {
		return pre;
	}

	public Iterable<Integer> post() {
		return post;
	}

	public Iterable<Integer> reversePost() {
		return reversePost;
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
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		DepthoFirstOrder firstOrder = new DepthoFirstOrder(digraph);
		System.out.println("pre:");
		for (int v : firstOrder.pre()) {
			System.out.print(v + " ");
		}
		System.out.println();
		System.out.println("post:");
		for (int v : firstOrder.post) {
			System.out.print(v + " ");
		}
		System.out.println();
		System.out.println("reversePost:");
		for (int v : firstOrder.reversePost()) {
			System.out.print(v + " ");
		}
	}

}
