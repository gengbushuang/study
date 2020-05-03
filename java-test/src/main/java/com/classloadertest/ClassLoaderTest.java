package com.classloadertest;

import sun.misc.JavaNetAccess;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.net.MalformedURLException;
import java.net.URL;

public class ClassLoaderTest {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        JavaNetAccess javaNetAccess = SharedSecrets.getJavaNetAccess();
        System.out.println(javaNetAccess);

        String path = "file:///C:/Users/gbs/git/study/java-test/classLoaderData/";
        URL url = new URL(path);
        MyClassLoader myClassLoader = new MyClassLoader(new URL[]{url});
        Class<?> aClass = myClassLoader.loadClass("A");
//        System.out.println(aClass);
//        Object o = aClass.newInstance();
        System.out.println(aClass.getClassLoader());
        System.out.println(aClass.getClassLoader().getParent());
        myClassLoader.loadClass("A");
        System.out.println("-------------------------------------");
        Class<?> aClass1 = myClassLoader.loadClass("com.classloadertest.ClassLoaderTest");
        System.out.println(aClass1);
        System.out.println(aClass1.getClassLoader());
        System.out.println(aClass1.getClassLoader().getParent());
        System.out.println("-------------------------------------");
        //类的装载步骤,加载-->链接-->初始化
        //因为loadClass里面的resolve为false代表不进行链接步骤
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println("执行ClassLoader加载类不会进行初始化静态块");
        classLoader.loadClass("com.classloadertest.B");
        System.out.println("执行forName加载类会进行初始化静态块");
        //因为forName里面的initialize为true代表执行初始化步骤
        Class.forName("com.classloadertest.B");
    }
}
