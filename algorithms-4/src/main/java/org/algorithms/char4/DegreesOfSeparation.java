package org.algorithms.char4;

public class DegreesOfSeparation {
	public static void main(String[] args) {
		SymbolGraph symbolGraph = new SymbolGraph("graph/routes.txt", " ");
		Graph graph = symbolGraph.g();
		String source = "JFK";
		if (!symbolGraph.contains(source)) {
			System.out.println(source + " not in database.");
			return;
		}

		int s = symbolGraph.index(source);
		BreadthFirstPaths bfs = new BreadthFirstPaths(graph, s);

		String sink = "HOU";
		System.out.println(sink);
		if (symbolGraph.contains(sink)) {
			int t = symbolGraph.index(sink);
			if (bfs.hasPathTo(t)) {
				for (int v : bfs.pathTo(t)) {
					System.out.println("  " + symbolGraph.name(v));
				}
			} else {
				System.out.println("Not connected");
			}
		} else {
			System.out.println("Not in database.");
		}
	}
}
