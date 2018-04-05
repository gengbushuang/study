package org.algorithms.util;

public class Stopwatch {

	private final long start;
	
	public Stopwatch() {
		this.start = System.currentTimeMillis();
	}
	
	public double elapsedTime() {
		long now = System.currentTimeMillis();
		return (now-start)/1000.0;
	}
}
