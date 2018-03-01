package org.java.thread.thread.per.message;

/**
 * 针对请求创建线程的类
 * 
 * @author gbs
 *
 */
public class Host {
	final Helper helper = new Helper();

	public void request(int count, char c) {
		System.out.println("     request(" + count + ", " + c + ") BEGIN");
		new Thread() {
			public void run() {
				helper.handle(count, c);
			}
		}.start();
		System.out.println("     request(" + count + ", " + c + ") END");
	}
}
