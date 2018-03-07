package org.java.thread.active.object;

/**
 * 创建主动对象的类
 * 
 * @author gbs
 *
 */
public class ActiveObjectFactory {

	public static ActiveObject createActiveObject(){
		return new ActiveObjectImpl();
	}
}
