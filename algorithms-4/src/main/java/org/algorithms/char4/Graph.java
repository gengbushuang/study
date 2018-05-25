package org.algorithms.char4;

import org.algorithms.char1.Bag;

public class Graph {

	private final int v;// 顶点的数目

	private int e; // 边的数目

	private Bag<Integer>[] adj;// 邻接表

	public Graph(int v) {
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
		adj[v].add(w); //
		adj[w].add(v);
		e++;
	}
	
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
}
