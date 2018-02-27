package org.java.thread.guarded.suspension;
/**
 * 请求类
 * @author gbs
 *
 */
public class Request {

	private final String name;
	
	public Request(String name){
		this.name = name;
	}

	@Override
	public String toString() {
		return "Request [name=" + name + "]";
	}
	
	
}
