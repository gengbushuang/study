package org.java.thread.active.object;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 委托ActiveObject来生成字符串的线程
 * 
 * @author gbs
 *
 */
public class MakerClientThread extends Thread {
	private final ActiveObject activeObject;
	private final char fillchar;

	public MakerClientThread(String name, ActiveObject activeObject) {
		super(name);
		this.activeObject = activeObject;
		this.fillchar = name.charAt(0);
	}

	@Override
	public void run() {

		try {
			for (int i = 0; true; i++) {
				Future<String> future = activeObject.makeString(i, fillchar);
				Thread.sleep(10);
				String value = future.get();
				System.out.println(Thread.currentThread().getName() + ": value=" + value);
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
