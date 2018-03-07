package org.java.thread.active.object.ch2;

/**
 * 仆人调用Servant角色的是SchedulerThread角色的线程。
 * SchedulerThread角色会从ActivationQueue角色取出一个MethodRequest角色并执行它。
 * 
 * @author gbs
 *
 */
public class Servant implements ActiveObject {

	@Override
	public Result<String> makeString(int count, char fillchar) {
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			buffer[i] = fillchar;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return new RealResult<String>(new String(buffer));
	}

	@Override
	public void displayString(String str) {
		System.out.println("displayString: " + str);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
