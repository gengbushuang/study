package org.algorithms.char4;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.char3.MySt;
import org.algorithms.util.Utils;

public class SymbolGraph {
	private MySt<String, Integer> st; // 符号名->索引
	private String[] keys;// 索引->符号名
	private Graph g;

	public SymbolGraph(String stream, String sp) {
		st = new MySt<>();
		String[] strings = Utils.readAllStrings(stream);// 第一遍构建索引
		for (String s : strings) {
			String[] a = s.split(sp);
			for (int i = 0; i < a.length; i++) {
				if (!st.contains(a[i])) {
					st.put(a[i], st.size());// 为每个不同的字符串关联一个索引
				}
			}
		}

		keys = new String[st.size()];// 用来获得顶点名的反向索引数组
		for (String name : st.keys()) {
			keys[st.get(name)] = name;
		}

		g = new Graph(st.size());// 第二遍构造图
		for (String s : strings) {
			String[] a = s.split(sp);
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++) {
				g.addEdge(v, st.get(a[i]));// 将每一行的顶点和该行的其他顶点相连
			}

		}
	}

	public boolean contains(String s) {
		return st.contains(s);
	}

	public int index(String s) {
		return st.get(s);
	}

	public String name(int v) {
		return keys[v];
	}

	public Graph g() {
		return g;
	}

	public static void main(String[] args) throws FileNotFoundException {
		SymbolGraph symbolGraph = new SymbolGraph("graph/movies.txt", "/");
		Graph graph = symbolGraph.g();

		Scanner scanner = Utils.getScanner("graph/movies.txt");

		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();
			int indexOf = nextLine.indexOf("/");
			if (indexOf < 0) {
				continue;
			}
			String source = nextLine.substring(0, indexOf);
			if (symbolGraph.contains(source)) {
				System.out.println(source);
				int s = symbolGraph.index(source);
				for (int v : graph.adj(s)) {
					System.out.println("   " + symbolGraph.name(v));
				}
			} else {
				System.out.println("input not contain '" + source + "'");
			}

		}
		scanner.close();
	}
}
