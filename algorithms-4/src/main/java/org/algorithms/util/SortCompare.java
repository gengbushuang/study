package org.algorithms.util;

import java.util.Arrays;
import java.util.Random;

import org.algorithms.char2.Insertion;
import org.algorithms.char2.Merage;
import org.algorithms.char2.Quick;
import org.algorithms.char2.Selection;
import org.algorithms.char2.Shell;


public class SortCompare {

	private static Random random;
	private static long seed;
	static {
		seed = System.currentTimeMillis();
		random = new Random(seed);
	}

	public static double time(String alg, Double[] a) {
		Stopwatch stopwatch = new Stopwatch();
		if ("Insertion".equals(alg)) {
			Insertion.sort(a);
		}
		if ("Selection".equals(alg)) {
			Selection.sort(a);
		}
		if("Shell".equals(alg)) {
			Shell.sort(a);
		}
		if("Merage".equals(alg)) {
			Merage.sort(a);
		}
		if("Quick".equals(alg)) {
			Quick.sort(a);
		}
		return stopwatch.elapsedTime();
	}

	public static double timeRandomInput(String alg, int n, int t) {
		double total = 0.0;
		Double[] a = new Double[n];
		for (int i = 0; i < t; i++) {
			for (int j = 0; j < n; j++) {
				a[j] = random.nextDouble();
			}
			total += time(alg, a);
		}
		return total;
	}

	public static void main(String[] args) {
		String alg1 = "Insertion";
		String alg2 = "Selection";
		int n = 1000;
		int t = 10;
		double t1 = timeRandomInput(alg1, n, t);
		double t2 = timeRandomInput(alg2, n, t);
		System.out.println(t1+","+t2);
		System.out.printf("For %d random Doubles\n  %s is", n, alg1);
		System.out.printf(" %.1f times faster than %s\n", t2 / t1, alg2);
	}
}
