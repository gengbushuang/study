package org.raft.common.server;

public class RaftServer {

	private CommittingThread commitingThread;

	public RaftServer() {
		commitingThread = new CommittingThread(this);
		new Thread(this.commitingThread).start();
	}

	static class CommittingThread implements Runnable {
		private RaftServer server;

		CommittingThread(RaftServer _server) {
			this.server = _server;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("ffff");
			}
		}
	}
}
