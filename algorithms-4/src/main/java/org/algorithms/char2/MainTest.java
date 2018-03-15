package org.algorithms.char2;


public class MainTest {

	public static void main(String[] args) {
		String ss = "S,O,R,T,E,X,A,M,P,L,E";
		String[] split = ss.split(",");
		Insertion.show(split);
		Insertion.sort(split);
		Insertion.show(split);
	}
}
