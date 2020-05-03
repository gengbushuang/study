classloader

BootStrapClassLoader:核心加载 C++编写，加载核心库java.*
ExtClassLoader:扩展加载 java编写，加载扩展库javax.* 路径->System.getProperty("java.ext.dirs")
AppClassLoader: java编写，加载程序所在目录 路径->System.getProperty("java.class.path");

类加载过程
    加载
        通过ClassLoader加载class文件字节码，生成Class对象
    链接
        校验
            检查加载的class正确性和安全性
        准备
            为类的变量分配存储空间并设置类变量的初始值
        解析
            jvm将常量池的符号引用转换为直接引用
    初始化
        执行类变量赋值和静态代码块
