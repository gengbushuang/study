package com.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PhantomReferenceTest {
//    public static void main(String[] args) {
//        int count = 10;
//        List<PhantomReference<byte[]>> list = new ArrayList<>(count);
//        final ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<byte[]>();
//        boolean isRun = true;
//        new Thread(() -> {
//            while (isRun) {
//                try {
//                    Reference<? extends byte[]> remove = referenceQueue.remove();
//                    System.out.println(remove);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        for (int i = 0; i < count; i++) {
//            byte[] buff = new byte[1024 * 1024];
//            PhantomReference<byte[]> pr = new PhantomReference<>(buff, referenceQueue);
//            list.add(pr);
//            try {
//                System.gc();
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final int MAX_SIZE = 10;

    private static final AtomicInteger resourceClosed = new AtomicInteger(0);

    private static void printStatus() {
        Runtime runtime = Runtime.getRuntime();
        long heapSize = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("HeapSize:%dKB, Closed:%d%n", heapSize >>> 10, resourceClosed.get());
    }

    private static void ensureGC() throws InterruptedException {
        System.gc();
        TimeUnit.SECONDS.sleep(1);
    }

    public static void main(String[] args)  throws InterruptedException {
        ReferenceQueue<ResourceOwner> referenceQueue = new ReferenceQueue<>();
        Deque<ResourceOwner> deque = new ArrayDeque<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            ResourceOwner owner = new ResourceOwner();
            new ResourceOwnerRef(owner, referenceQueue);
            deque.push(owner);
        }
        Thread closeThread =
                new Thread(
                        () -> {
                            while (resourceClosed.get() < MAX_SIZE) {
                                try {
                                    ResourceOwnerRef ref = (ResourceOwnerRef) referenceQueue.remove();
                                    ref.clear();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        closeThread.setDaemon(true);
        closeThread.start();
        while (!deque.isEmpty()) {
            deque.pop();
            ensureGC();
            printStatus();
        }
    }

    private static final class ResourceMustClose implements AutoCloseable {
        private static final AtomicInteger idCounter = new AtomicInteger(0);
        private final int id;
        private byte[] bytes = new byte[1024 * 1024];

        ResourceMustClose() {
            this.id = idCounter.incrementAndGet();
            System.out.printf("resource[%d] allocated.%n", id);
        }

        @Override
        public void close() {
            bytes = null;
            resourceClosed.getAndIncrement();
            System.out.printf("resource[%d] closed.%n", id);
        }
    }

    private static final class ResourceOwner {
        private final ResourceMustClose resourceMustClose = new ResourceMustClose();

        ResourceMustClose getResourceMustClose() {
            return resourceMustClose;
        }
    }

    private static final class ResourceOwnerRef extends PhantomReference<ResourceOwner> {

        private static final CopyOnWriteArraySet<ResourceOwnerRef> refSet = new CopyOnWriteArraySet<>();

        private final Runnable thunk;

        ResourceOwnerRef(ResourceOwner referent, ReferenceQueue<? super ResourceOwner> q) {
            super(referent, q);
            this.thunk = referent.getResourceMustClose()::close;
            refSet.add(this);
        }

        @Override
        public void clear() {
            if (thunk != null) thunk.run();
            refSet.remove(this);
            super.clear();
        }
    }
}
