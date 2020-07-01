java对象大小
    对象头(Header 32/64)
        mark(4/8字节)
            对象的hashCode(25bit)
            分代年龄(4bit)
                对象分代GC的年龄。
            是否偏向锁(1bit)
                是 0
                否 1
            锁标志位(2bit)
                无锁 01
                轻量级锁 00
                重量级锁 10

                锁状态 无锁->偏向锁->轻量级锁->重量级锁



        klass指针(4/8字节，压缩的话64位4字节)
        如果是对象数组还有一个数组长度length(4字节)

    实例数据(Body)--这部分是真正的业务数据，对象中的实例字段
        byte 1字节
        int 4字节
        boolean 1字节
        float 4字节
        char 2字节
        long 8字节
        short 2字节
        double 8字节
        ref (4/8字节,如果64位开启压缩4字节)

    实例填充(Padding)
        要求对象大小必须是8整数，让对象在内存中地址空间达到8的整数倍而额外占用的字节数

