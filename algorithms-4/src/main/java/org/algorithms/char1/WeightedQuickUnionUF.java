package org.algorithms.char1;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import org.algorithms.util.Utils;

public class WeightedQuickUnionUF {
	private int[] ids;// 分量ID(以触点作为索引)
	private int[] sz;// 用来记录这个索引下树大小
	private int count;// 分量数量

	public WeightedQuickUnionUF(int N) {// 以整数标识(0到N-1)初始化N个触点
		this.count = N;
		this.ids = new int[N];
		for (int i = 0; i < N; i++) {
			this.ids[i] = i;
		}
		this.sz = new int[N];
		Arrays.fill(this.sz, 1);
	}

	public int[] getIds() {
		return ids;
	}

	public void union(int p, int q) {// 在p和q之间添加一条连接
		int idp = find(p);
		int idq = find(q);
		if (idp == idq) {
			return;
		}
		// 判断这个两个节点下的那个树小，树小值指向树大的下标，等于小树连接大树
		if (sz[idp] < sz[idq]) {
			ids[idp] = idq;
			sz[idq] += sz[idp];
		} else {
			ids[idq] = idp;
			sz[idp] += sz[idq];
		}
		count--;
	}

	public int find(int p) {// p所在的分量的标识符(0到N-1)
		while (p != ids[p]) {
			p = ids[p];// 找寻父类数字
		}
		return p;
	}

	public boolean connected(int p, int q) {// 如果p和q存在于同一个分量中则返回true
		return find(p) == find(q);
	}

	public int count() {// 连通分量的数量
		return count;
	}

	public static void main(String[] args) throws FileNotFoundException {
		long n1 = System.currentTimeMillis();
		// Scanner scanner = Utils.getScanner("tinyUF.txt");
		// Scanner scanner = Utils.getScanner("mediumUF.txt");
		Scanner scanner = Utils.getScanner("largeUF.txt");
		int N = scanner.nextInt();
		WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
		while (scanner.hasNext()) {
			int p = scanner.nextInt();
			int q = scanner.nextInt();
			// if(uf.connected(p, q)) {
			// continue;
			// }
			uf.union(p, q);
			// System.out.println(p+" "+q);
		}
		System.out.println(uf.count + " components");
		// System.out.println(Arrays.toString(uf.getIds()));
		System.out.println(System.currentTimeMillis() - n1 + "毫秒");
	}
}
