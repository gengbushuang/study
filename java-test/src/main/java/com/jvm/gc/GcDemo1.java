package com.jvm.gc;

/**
 * -XX:NewSize=5M -XX:MaxNewSize=5M -XX:InitialHeapSize=10M -XX:MaxHeapSize=10M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:java-test/src/main/java/com/jvm/gc/gc-demo1.log
 * <p>
 * InitialHeapSize初始化堆大小
 * MaxHeapSize最大堆大小
 * NewSize初始化新生代大小
 * MaxNewSize最大新生代大小
 * PretenureSizeThreshold大对象阈值
 * <p>
 * PrintGCDetails打印详细的gc日志
 * PrintGCTimeStamps打印出来每次GC发生的时间
 * Xloggc设置gc日志写入一个磁盘文件
 * <p>
 * <p>
 * 堆内存分配10MB内存，新生代内存5MB，其中Eden占4MB，每个Survivor占0.5MB，老年代内存5MB，大对象超过10MB直接进入老年代
 * UseParNewGC年轻代垃圾回收器
 * UseConcMarkSweepGC老年代垃圾回收器
 */
public class GcDemo1 {

    public static void main(String[] args) {
        //在Eden分配1MB内存
        byte[] array1 = new byte[1024 * 1024];
        //上个1MB内存引用失效
        //在Eden分配1MB内存
        array1 = new byte[1024 * 1024];
        //上个1MB内存引用失效
        //在Eden分配1MB内存
        array1 = new byte[1024 * 1024];
        //上个1MB内存引用失效
        array1 = null;
        //现在Eden里面有3MB的内存占用，没有任何引用，变成垃圾可回收对象

        //在继续往Eden分配2MB内存，Eden现在已经有3MB多内存占用，Eden总空间4MB
        //超过总空间，开始进行新生代内存GC，回收Eden
        byte[] array2 = new byte[2 * 1024 * 1024];

    }

    /*
    Java HotSpot(TM) 64-Bit Server VM (25.151-b12) for windows-amd64 JRE (1.8.0_151-b12), built on Sep  5 2017 19:33:46 by "java_re" with MS VC++ 10.0 (VS2010)
Memory: 4k page, physical 8308108k(2931180k free), swap 13026700k(4382776k free)
CommandLine flags: -XX:InitialHeapSize=10485760 -XX:MaxHeapSize=10485760 -XX:MaxNewSize=5242880 -XX:NewSize=5242880 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=10485760 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC
0.094: [GC (Allocation Failure) 0.094: [ParNew: 4005K->512K(4608K), 0.0009139 secs] 4005K->653K(9728K), 0.0010474 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

//Allocation Failure内存分配失败，当Eden内存(4MB)分配了3MB多的时候，要继续分配2MB的内存，内存不已经不够要进行新生代的垃圾回收(4005K->512K(4608K)),回收后还有512k的内存存活

Heap
 par new generation   total 4608K, used 2642K [0x00000000ff600000, 0x00000000ffb00000, 0x00000000ffb00000)

 //回收后，2MB内存就可用分配进去了

  eden space 4096K,  52% used [0x00000000ff600000, 0x00000000ff814930, 0x00000000ffa00000)
  from space 512K, 100% used [0x00000000ffa80000, 0x00000000ffb00000, 0x00000000ffb00000)

  //存活的512k内存刚好可用放到Survivor中，占用100%

  to   space 512K,   0% used [0x00000000ffa00000, 0x00000000ffa00000, 0x00000000ffa80000)
 concurrent mark-sweep generation total 5120K, used 141K [0x00000000ffb00000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2657K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 286K, capacity 386K, committed 512K, reserved 1048576K
     */
}
