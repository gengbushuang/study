package com.jvm.gc;

/**
 * -XX:NewSize=10M -XX:MaxNewSize=10M -XX:InitialHeapSize=20M -XX:MaxHeapSize=20M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:java-test/src/main/java/com/jvm/gc/gc-demo2.log
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
public class GcDemo2 {

    public static void main(String[] args) {
        //在Eden分配2MB内存
        byte[] array1 = new byte[2 * 1024 * 1024];
        //上个2MB内存引用失效
        //在Eden分配2MB内存
        array1 = new byte[2 * 1024 * 1024];
        //上个2MB内存引用失效
        //在Eden分配2MB内存
        array1 = new byte[2 * 1024 * 1024];
        //上个2MB内存引用失效
        array1 = null;
        //现在Eden里面有6MB的内存占用，没有任何引用，变成垃圾可回收对象

        //在Eden分配128KB内存
        byte[] array2 = new byte[128 * 1024];

        //在继续往Eden分配2MB内存，Eden现在已经有6MB多内存占用，Eden总空间8MB
        //超过总空间，开始进行新生代内存GC，回收Eden
        byte[] array3 = new byte[2 * 1024 * 1024];
        //第一次GC后，array2存活下来了，年龄1
        //新生代现在有array3+array2内存

        //在Eden分配2MB内存
        array3 = new byte[2 * 1024 * 1024];
        //在Eden分配2MB内存
        array3 = new byte[2 * 1024 * 1024];
        //在Eden分配128KB内存
        array3 = new byte[128 * 1024];
        array3 = null;

        //新生代现在有128KB+2MB+2MB+2MB+128KB,总共6MB多内存，只有array2的128KB还在Survivor中继续被引用，其他都是可用回收的

        ///在Eden分配2MB内存
        byte [] array4 = new byte[2 * 1024 * 1024];
        //在继续分配这个2MB内存时发现Eden已经6MB多了，超过8MB又要进行一次垃圾回收

    }

    /*
Java HotSpot(TM) 64-Bit Server VM (25.151-b12) for windows-amd64 JRE (1.8.0_151-b12), built on Sep  5 2017 19:33:46 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 8308108k(2781560k free), swap 13026700k(4058008k free)
CommandLine flags: -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=10485760 -XX:MaxTenuringThreshold=15 -XX:NewSize=10485760 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=10485760 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC
0.090: [GC (Allocation Failure) 0.090: [ParNew: 7420K->790K(9216K), 0.0008240 secs] 7420K->790K(19456K), 0.0009682 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

//第一次GC后还有790K存活对象，正好可以放到Survivor中，占用1024KB/790KKB，超过50%

0.092: [GC (Allocation Failure) 0.092: [ParNew: 7095K->0K(9216K), 0.0022260 secs] 7095K->778K(19456K), 0.0023112 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

//第二次GC后发现没有存活对象了，连array2引用的128KB内存也不见了
//concurrent mark-sweep generation total 10240K, used 778K 看到这个记录发现是被移动到了老年代，触发了动态年龄规则只要Survivor超过50%，就算没有到15年龄也会移动到老年代

Heap
 par new generation   total 9216K, used 2212K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 8192K,  27% used [0x00000000fec00000, 0x00000000fee290e0, 0x00000000ff400000)
  from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
  to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 concurrent mark-sweep generation total 10240K, used 778K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2659K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 286K, capacity 386K, committed 512K, reserved 1048576K


     */
}
