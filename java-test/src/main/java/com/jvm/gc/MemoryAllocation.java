package com.jvm.gc;

public class MemoryAllocation {

	private static final int _1MB = 1024*1024;
	
	/**
	 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
	 */
	
	/*
[GC (Allocation Failure) [DefNew: 7144K->430K(9216K), 0.0050854 secs] 7144K->6574K(19456K), 0.0051269 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 def new generation   total 9216K, used 4609K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  51% used [0x00000007bec00000, 0x00000007bf014930, 0x00000007bf400000)
  from space 1024K,  42% used [0x00000007bf500000, 0x00000007bf56bb60, 0x00000007bf600000)
  to   space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
 tenured generation   total 10240K, used 6144K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  60% used [0x00000007bf600000, 0x00000007bfc00030, 0x00000007bfc00200, 0x00000007c0000000)
 Metaspace       used 2753K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 298K, capacity 386K, committed 512K, reserved 1048576K
	 */
	public static void testAllocation(){
		byte[] allacation1,allacation2,allacation3,allacation4;
		allacation1 = new byte[2 * _1MB];
		allacation2 = new byte[2 * _1MB];
		allacation3 = new byte[2 * _1MB];
		allacation4 = new byte[4 * _1MB];
	}
	
	
	/**
	 * VM参数:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728 -XX:+UseSerialGC 
	 * 大对象直接进入老年代(PretenureSizeThreshold设置为3M)
	 * PretenureSizeThreshold这个只对Serial和ParNew两款收集器有效
	 */
	
	/*
	Heap
 def new generation   total 9216K, used 1164K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  14% used [0x00000007bec00000, 0x00000007bed23098, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
  to   space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
 tenured generation   total 10240K, used 4096K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  40% used [0x00000007bf600000, 0x00000007bfa00010, 0x00000007bfa00200, 0x00000007c0000000)
 Metaspace       used 2753K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 298K, capacity 386K, committed 512K, reserved 1048576K
	 */
	public static void testPretenureSizeThreshold(){
		byte [] allocation;
		allocation = new byte[4*_1MB];
	}
	
	/**
	 * VM参数:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution -XX:+UseSerialGC
	 */
	public static void testTenuringThreshold(){
		byte[] allacation1,allacation2,allacation3;
		allacation1 = new byte[_1MB/4];
		//什么时候进入老年代取决于XX:MaxTenuringThreshold设置
		allacation2 = new byte[4*_1MB];
		allacation3 = new byte[4*_1MB];
		allacation3=null;
		allacation3 = new byte[4*_1MB];
	}
	
	/**
	 * VM参数:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution -XX:+UseSerialGC
	 */
	public static void testTenuringThreshold2(){
		byte[] allacation1,allacation2,allacation3,allacation4;
		allacation1 = new byte[_1MB/4];
		//什么时候进入老年代取决于XX:MaxTenuringThreshold设置
		allacation2 = new byte[_1MB/4];
		allacation3 = new byte[4*_1MB];
		allacation4 = new byte[4*_1MB];
		allacation4=null;
		allacation4 = new byte[4*_1MB];
	}
	
	public static void main(String[] args) {
		MemoryAllocation.testTenuringThreshold2();
	}
	
}
