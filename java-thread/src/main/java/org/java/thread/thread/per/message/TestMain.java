package org.java.thread.thread.per.message;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class TestMain {

	public static void main(String[] args) {
		System.out.println("main BEGIN");
//		Host host = new Host();
		//runable接口实现线程
//		HostRunable host = new HostRunable();
		//ThreadFactory线程工厂
//		HostThreadFactory host = new HostThreadFactory(new ThreadFactory() {
//			@Override
//			public Thread newThread(Runnable r) {
//				Thread result=new Thread(r);  
//				return result;
//			}
//		});
		//Executor实现
//		HostExecutor host = new HostExecutor(new Executor() {
//			@Override
//			public void execute(Runnable command) {
//				new Thread(command).start();
//			}
//		});
		//ExecutorService实现
//		ExecutorService executorService = Executors.newCachedThreadPool();
//		HostExecutorService host = new HostExecutorService(executorService);
//		try{
//			host.request(10, 'A');
//			host.request(20, 'B');
//			host.request(30, 'C');
//		}finally{
//			executorService.shutdown();
//		}
		//ScheduledExecutorService实现
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
		HostScheduledExecutorService host = new HostScheduledExecutorService(scheduledExecutorService);
		try{
		host.request(10, 'A');
		host.request(20, 'B');
		host.request(30, 'C');
		}finally{
			scheduledExecutorService.shutdown();
		}
		System.out.println("main END");
	}
}
