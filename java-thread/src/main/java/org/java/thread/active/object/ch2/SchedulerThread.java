package org.java.thread.active.object.ch2;

/**
 * 调用execute方法处理MethodRequest对象的类
 * 
 * @author gbs
 *         SchedulerThread角色负责将Proxy角色传递来的MethodRequest角色传递给ActivationQueue角色以及从ActivationQueue角色取出并执行MethodRequest角色这两项工作
 */
public class SchedulerThread extends Thread {
	private final ActivationQueue queue;

	public SchedulerThread(ActivationQueue queue) {
		this.queue = queue;
	}

	public void invoke(MethodRequest request) {
		queue.putRequest(request);
	}

	@Override
	public void run() {
		while (true) {
			MethodRequest request = queue.takeRequest();
			request.execute();
		}
	}
}
