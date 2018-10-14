package org.algorithms.char4;

import org.algorithms.char1.Bag;

/**
 * 有向图
 * 
 * @author gbs
 *
 */
public class Digraph {
	private final int v;// 顶点的数目

	private int e; // 边的数目

	private Bag<Integer>[] adj;// 邻接表

	public Digraph(int v) {
		this.v = v;
		this.e = 0;
		this.adj = (Bag<Integer>[]) new Bag[v];// 创建邻接表
		for (int i = 0; i < v; i++) {
			this.adj[i] = new Bag<>();
		}
	}

	public int v() {
		return v;
	}

	public int e() {
		return e;
	}

	public void addEdge(int v, int w) {
		adj[v].add(w);
		e++;
	}

	public Iterable<Integer> adj(int v) {
		return adj[v];
	}

	public Digraph reverse() {
		Digraph r = new Digraph(v);
		for (int i = 0; i < v; i++) {
			for (int w : adj[i]) {
				r.addEdge(w, i);
			}
		}
		return r;
	}
}
