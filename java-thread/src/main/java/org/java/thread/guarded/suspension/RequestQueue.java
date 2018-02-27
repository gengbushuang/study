package org.java.thread.guarded.suspension;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 存放请求的类
 * 
 * @author gbs
 *
 */
public class RequestQueue {

	private final Queue<Request> requests = new LinkedList<Request>();

	public RequestQueue() {

	}

	public synchronized Request getRequest() {
		while (requests.peek() == null) {
			try {
				System.out.println("getRequest wait start");
				wait();
				System.out.println("getRequest wait end");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return requests.remove();
	}

	public synchronized void putRequest(Request request) {
		requests.offer(request);
		System.out.println("putRequest notifyAll start");
		notifyAll();
		System.out.println("putRequest notifyAll end");
		//requests.offer(request);
		//notifyAll();
	}
}
