package org.java.thread.active.object.ch2;
/**
 * 表示请求的抽象类
 * @author gbs
 *
 */
public abstract class MethodRequest<T> {

	protected final Servant servant;
	protected final FutureResult<T> future;
	
	protected MethodRequest(Servant servant,FutureResult<T> future){
		this.servant = servant;
		this.future = future;
	}
	
	public abstract void execute();
}
