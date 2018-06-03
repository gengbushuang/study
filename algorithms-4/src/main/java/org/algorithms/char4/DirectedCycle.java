package org.algorithms.char4;

import org.algorithms.char1.Stack;

/**
 * 判断是否有向无环图
 * 
 * @author gbs
 *
 */
public class DirectedCycle {

	private boolean[] marked;
	private int edgeTo[];
	private boolean[] onStack;// 递归调用栈上的所有顶点
	private Stack<Integer> cycle;

	public DirectedCycle(Digraph g) {
		marked = new boolean[g.v()];
		edgeTo = new int[g.v()];
		onStack = new boolean[g.v()];
		for (int v = 0; v < g.v(); v++) {
			if (!marked[v]) {
				dfs(g, v);
			}
		}
	}

	private void dfs(Digraph g, int v) {
		marked[v] = true;
		onStack[v] = true;

		for (int w : g.adj(v)) {
			if (hasCycle()) {
				return;
			} else if (!marked[w]) {
				edgeTo[w] = v;
				dfs(g, w);
			} else if (onStack[w]) {
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
		}

		onStack[v] = false;
	}

	// 是否含有有向环
	public boolean hasCycle() {
		return cycle != null;
	}

	// 有向环中的所有顶点
	public Iterable<Integer> cycle() {
		return cycle;
	}
}
