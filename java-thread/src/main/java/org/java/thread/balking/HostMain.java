package org.java.thread.balking;

import java.util.concurrent.TimeoutException;

public class HostMain {
	public static void main(String[] args) {
		Host host = new Host(10000);

		System.out.println("execute BEGIN");
		try {
			host.execute();
		} catch (TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
