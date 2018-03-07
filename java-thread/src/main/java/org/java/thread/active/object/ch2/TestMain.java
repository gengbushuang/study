package org.java.thread.active.object.ch2;

public class TestMain {
	public static void main(String[] args) {
		ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
		
		new MakerClientThread("Alice", activeObject).start();
		new MakerClientThread("Bobby", activeObject).start();
		new DisplayClientThread("Chris", activeObject).start();
	}
}
