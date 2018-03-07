package org.java.thread.active.object.ch2;
/**
 * 创建"主动对象"的类
 * @author gbs
 *
 */
public class ActiveObjectFactory {
	public static ActiveObject createActiveObject(){
		Servant servant = new Servant();
		ActivationQueue queue = new ActivationQueue();
		SchedulerThread schedulerThread = new SchedulerThread(queue);
		Proxy proxy = new Proxy(schedulerThread,servant);
		schedulerThread.start();
		return proxy;
	}
}
