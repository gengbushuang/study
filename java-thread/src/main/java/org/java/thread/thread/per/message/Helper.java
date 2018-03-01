package org.java.thread.thread.per.message;

/**
 * 提供字符显示功能的被动类
 * 
 * @author gbs
 *
 */
public class Helper {

	public void handle(int count, char c) {
		System.out.println("     handle(" + count + ", " + c + ") BEGIN");
		for (int i = 0; i < count; i++) {
			System.out.print(c);
			slowly();
		}
		System.out.println("");
		System.out.println("     handle(" + count + ", " + c + ") END");
	}

	private void slowly() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
