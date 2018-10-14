package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.char1.Bag;
import org.algorithms.util.Utils;


public class CC {
	private boolean[] marked;
	private int[] id;
	private int count;

	public CC(Graph g) {
		marked = new boolean[g.v()];
		id = new int[g.v()];
		for (int s = 0; s < g.v(); s++) {
			if (!marked[s]) {
				dfs(g, s);
				count++;
			}
		}
	}

	private void dfs(Graph g, int v) {
		marked[v] = true;
		id[v] = count;
		for (int e : g.adj(v)) {
			if (!marked[e]) {
				dfs(g, e);
			}
		}
	}

	public boolean connected(int v, int w) {
		return id[v] == id[w];
	}

	public int id(int v) {
		return id[v];
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
		
		CC cc = new CC(graph);
		int m = cc.count;
		System.out.println(m+" components");
		
		Bag<Integer>[] components;
		
		components = (Bag<Integer>[])new Bag[m];
		
		for(int i = 0;i<m;i++){
			components[i] = new Bag<Integer>();
		}
		
		for(int v = 0;v<graph.v();v++){
			components[cc.id(v)].add(v);
		}
		
		for(int i = 0;i<m;i++){
			for(int v:components[i]){
				System.out.print(v +" ");
			}
			System.out.println();
		}
	}
}
