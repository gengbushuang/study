package org.java.thread.active.object.ch2;

/**
 * 定义"主动对象"的接口
 * 
 * @author gbs
 *
 */
public interface ActiveObject {
	public Result<String> makeString(int count, char fillchar);

	public void displayString(String str);
}
