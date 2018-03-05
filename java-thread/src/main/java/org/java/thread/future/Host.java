package org.java.thread.future;

/**
 * 请求返回FutureData的实例类
 * 
 * @author gbs
 *
 */
public class Host {

	public Data request(final int count, final char c) {
		System.out.println("   request(" + count + ", " + c + ") BEGIN");
		final FutureData futureData = new FutureData();

		new Thread() {
			public void run() {
				RealData realData = new RealData(count, c);
				futureData.setRealData(realData);
			}
		}.start();
		System.out.println("   request(" + count + ", " + c + ") END");
		return futureData;
	}

}
