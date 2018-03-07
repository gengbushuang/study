package org.java.thread.active.object.ch2;

/**
 * porxy角色负责讲方法调用转换为MethodRequest角色对象。转换后的MethodRequest角色会被传递给SchedulerThread角色。
 * 
 * @author gbs
 *
 */
public class Proxy implements ActiveObject {

	private final SchedulerThread schedulerThread;
	private final Servant servant;

	public Proxy(SchedulerThread schedulerThread, Servant servant) {
		this.schedulerThread = schedulerThread;
		this.servant = servant;
	}

	@Override
	public Result<String> makeString(int count, char fillchar) {
		FutureResult<String> future = new FutureResult<String>();
		schedulerThread.invoke(new MakeStringRequest(servant, future, count, fillchar));
		return future;
	}

	@Override
	public void displayString(String str) {
		schedulerThread.invoke(new DisplayStringRequest(servant, str));
	}

}
