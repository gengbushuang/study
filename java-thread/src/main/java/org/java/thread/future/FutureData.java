package org.java.thread.future;

/**
 * 表示RealData的"提货单"的类。
 * 
 * @author gbs
 *
 */
public class FutureData implements Data {

	private RealData realData;
	private boolean ready = false;

	public synchronized void setRealData(RealData realData) {
		if (ready) {
			return;
		}
		this.realData = realData;
		this.ready = true;
		notifyAll();
	}

	@Override
	public synchronized String getContent() {
		while (!ready) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return realData.getContent();
	}

}
