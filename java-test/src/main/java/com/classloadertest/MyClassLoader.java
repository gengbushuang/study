package com.classloadertest;

import sun.misc.Resource;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PrivilegedExceptionAction;
import java.util.jar.Manifest;

public class MyClassLoader extends URLClassLoader {

    final URLClassPath ucp = SharedSecrets.getJavaNetAccess().getURLClassPath(this);

    public MyClassLoader(URL[] urls) {
        super(urls);
    }
//覆写下面的方法会破坏双亲委派规则
//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//        return this.loadClass(name, false);
//    }
//
//    @Override
//    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        URL resource = this.ucp.findResource(name, resolve);
//        Class var5 = this.findLoadedClass(name);
//        return super.loadClass(name, resolve);
//    }

    //最好用下面方法进行覆写
    //loadClass的方法里面在找不到情况下会执行findClass方法
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
//        super.getResource()
        Resource res = ucp.getResource(path, false);
        if (res != null) {
            try {
                return this.defineClass(name,res);
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        } else {
            return super.findClass(name);
        }
    }

    private Class<?> defineClass(String name, Resource res) throws IOException {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
//        if (i != -1) {
//            String pkgname = name.substring(0, i);
//            // Check if package already loaded.
//            Manifest man = res.getManifest();
//            definePackageInternal(pkgname, man, url);
//        }
        // Now read the class bytes and define the class
        java.nio.ByteBuffer bb = res.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, bb, cs);
        } else {
            byte[] b = res.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, b, 0, b.length, cs);
        }
    }
}
