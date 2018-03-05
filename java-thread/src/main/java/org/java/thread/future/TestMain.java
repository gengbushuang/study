package org.java.thread.future;

public class TestMain {
	public static void main(String[] args) {
		long n = System.currentTimeMillis();
		System.out.println("main BEGIN");
		//Host host = new Host();
		//java自带的future
		HostJavaFuture host = new  HostJavaFuture();
		Data data1 = host.request(10, 'A');
		Data data2 = host.request(20, 'B');
		Data data3 = host.request(30, 'C');

		System.out.println("main otherJob BEGIN");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("main otherJob END");
		System.out.println("data1 = " + data1.getContent() + "," + (System.currentTimeMillis() - n) + "秒");
		System.out.println("data2 = " + data2.getContent() + "," + (System.currentTimeMillis() - n) + "秒");
		System.out.println("data3 = " + data3.getContent() + "," + (System.currentTimeMillis() - n) + "秒");
	}
}
