package org.java.thread.producer.consumer;

public class Something {

	public static void method(long x) throws InterruptedException {
		if(x!=0) {
			Object object = new Object();
			synchronized(object) {
				object.wait(x);
			}
		}
	}
	
	public static void main(String[] args) {
		long n = System.currentTimeMillis();
		try {
			Something.method(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-n);
	}
}
