package org.machine.learning.algorithm.util;

public class Utils {

	public static float[] createFloat(String[] lines, int n) {
		float[] fs = new float[n];
		for (int i = 0; i < n; i++) {
			fs[i] = Float.parseFloat(lines[i]);
		}
		return fs;
	}

	public static double[] createDouble(String[] lines, int n) {
		double[] ds = new double[n];
		for (int i = 0; i < n; i++) {
			ds[i] = Double.parseDouble(lines[i]);
		}
		return ds;
	}
}
