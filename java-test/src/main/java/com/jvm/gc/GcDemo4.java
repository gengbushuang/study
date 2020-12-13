package com.jvm.gc;

/**
 * -XX:NewSize=10M -XX:MaxNewSize=10M -XX:InitialHeapSize=20M -XX:MaxHeapSize=20M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:java-test/src/main/java/com/jvm/gc/gc-demo4.log
 * <p>
 * InitialHeapSize初始化堆大小
 * MaxHeapSize最大堆大小
 * NewSize初始化新生代大小
 * MaxNewSize最大新生代大小
 * PretenureSizeThreshold大对象阈值
 * MaxTenuringThreshold对象达到15岁才会直接进入老年代
 * <p>
 * PrintGCDetails打印详细的gc日志
 * PrintGCTimeStamps打印出来每次GC发生的时间
 * Xloggc设置gc日志写入一个磁盘文件
 * <p>
 * <p>
 * 堆内存分配20MB内存，新生代内存10MB，其中Eden占8MB，每个Survivor占1MB，老年代内存10MB，大对象超过10MB直接进入老年代
 * UseParNewGC年轻代垃圾回收器
 * UseConcMarkSweepGC老年代垃圾回收器
 */
public class GcDemo4 {



    public static void main(String[] args) {
        byte[] array1 = new byte[4 * 1024 * 1024];
        array1=null;
        byte[] array2 = new byte[2 * 1024 * 1024];
        byte[] array3 = new byte[2 * 1024 * 1024];
        byte[] array4 = new byte[2 * 1024 * 1024];

        byte[] array5 = new byte[128 * 1024];
        byte[] array6 = new byte[2 * 1024 * 1024];

        byte[] array7 = new byte[4 * 1024 * 1024];

    }

    /*
Java HotSpot(TM) 64-Bit Server VM (25.151-b12) for windows-amd64 JRE (1.8.0_151-b12), built on Sep  5 2017 19:33:46 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 8308108k(2543760k free), swap 13026700k(4277044k free)
CommandLine flags: -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=10485760 -XX:MaxTenuringThreshold=15 -XX:NewSize=10485760 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=10485760 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC
0.089: [GC (Allocation Failure) 0.089: [ParNew: 7420K->664K(9216K), 0.0013228 secs] 7420K->2714K(19456K), 0.0014707 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]



Heap
 par new generation   total 9216K, used 2795K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 8192K,  26% used [0x00000000fec00000, 0x00000000fee14930, 0x00000000ff400000)
  from space 1024K,  64% used [0x00000000ff500000, 0x00000000ff5a6388, 0x00000000ff600000)
  to   space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
 concurrent mark-sweep generation total 10240K, used 2050K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2660K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 286K, capacity 386K, committed 512K, reserved 1048576K


     */
}
