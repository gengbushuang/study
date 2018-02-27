package org.java.thread.balking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Data {

	private final String filename;
	private String content;
	private boolean changed;

	public Data(String filename, String content) {
		this.filename = filename;
		this.content = content;
	}

	public synchronized void change(String newContent) {
		this.content = newContent;
		changed = true;
	}

	public synchronized void save() throws IOException {
		if (!changed) {
			return;
		}
		doSave();
		changed = false;
	}

	public void doSave() throws IOException {
		System.out.println(Thread.currentThread().getName() + " calls doSave, content=" + this.content);
		Writer writer = new FileWriter(filename);
		writer.write(content);
		writer.close();
	}
}
