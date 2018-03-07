package org.java.thread.active.object;

import java.util.concurrent.Future;

/**
 * 定义主动对象的接口(API)的接口
 * @author gbs
 *
 */
public interface ActiveObject {

	public Future<String> makeString(int count,char fillchar);
	public void displayString(String string);
	public void shutdown();
}
