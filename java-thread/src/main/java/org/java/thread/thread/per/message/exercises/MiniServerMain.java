package org.java.thread.thread.per.message.exercises;

import java.io.IOException;

public class MiniServerMain {
	public static void main(String[] args) {
		try {
			new MiniServer(8888).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
