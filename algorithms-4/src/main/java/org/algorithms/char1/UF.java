package org.algorithms.char1;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.algorithms.util.Utils;

public class UF {
	private int[] ids;// 分量ID(以触点作为索引)
	private int count;// 分量数量

	public UF(int N) {// 以整数标识(0到N-1)初始化N个触点
		this.count = N;
		this.ids = new int[N];
		for (int i = 0; i < N; i++) {
			this.ids[i] = i;
		}
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
		//将p的分量重命名为q的名称
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == idp) {
				ids[i] = idq;
			}
		}
		//System.out.println(count);
		count--;
	}

	public int find(int p) {// p所在的分量的标识符(0到N-1)
		return ids[p];
	}

	public boolean connected(int p, int q) {// 如果p和q存在于同一个分量中则返回true
		return find(p) == find(q);
	}

	public int count() {// 连通分量的数量
		return count;
	}
	
	////改进union方法查找，每次不同都要重新遍历所有的数组，非常耗时
	public int find2(int p) {
		while(p!=ids[p]) {
			p=ids[p];//找寻父类数字
		}
		return p;
	}
	
	public void union2(int p,int q) {
		int idp = find2(p);
		int idq = find2(q);
		if (idp == idq) {
			return;
		}
		//指向父类下标
		ids[idp] = idq;
		//System.out.println(count);
		count--;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		long n1 = System.currentTimeMillis();
//		Scanner scanner = Utils.getScanner("tinyUF.txt");
//		Scanner scanner = Utils.getScanner("mediumUF.txt");
		Scanner scanner = Utils.getScanner("largeUF.txt");
		int N = scanner.nextInt();
		UF uf = new UF(N);
		while(scanner.hasNext()) {
			int p = scanner.nextInt();
			int q = scanner.nextInt();
//			if(uf.connected(p, q)) {
//				continue;
//			}
			uf.union2(p, q);
			//System.out.println(p+" "+q);
		}
		System.out.println(uf.count+" components");
		//System.out.println(Arrays.toString(uf.getIds()));
		System.out.println(System.currentTimeMillis()-n1+"毫秒");
	}
}
