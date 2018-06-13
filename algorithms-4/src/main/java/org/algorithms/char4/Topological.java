package org.algorithms.char4;
/**
 * 拓扑排序
 * @author gbs
 *
 */
public class Topological {

	private Iterable<Integer> order; // 顶点的拓扑顺序

	public Topological(Digraph g) {
		DirectedCycle cycle = new DirectedCycle(g);
		if (!cycle.hasCycle()) {
			DepthoFirstOrder firstOrder = new DepthoFirstOrder(g);
			order = firstOrder.reversePost();
		}
	}

	public Iterable<Integer> order() {
		return order;
	}

	public boolean isDAG() {
		return order != null;
	}
	
	public static void main(String[] args) {
		SymbolDigraph symbolDigraph = new SymbolDigraph("graph/jobs.txt", "/");
		
		Topological top = new Topological(symbolDigraph.g());
		for(int v:top.order){
			System.out.println(symbolDigraph.name(v));
		}
	}
}
