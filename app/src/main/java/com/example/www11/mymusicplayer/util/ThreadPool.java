package com.example.www11.mymusicplayer.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池.
 */
public class ThreadPool {
    /**
     * 一段时间内线程可以复用.
     */
    private static ExecutorService sThreadPool;

    static {
        sThreadPool = Executors.newCachedThreadPool();
    }

    static ExecutorService getThreadPool() {
        return sThreadPool;
    }

    public static void shutDownPool() {
        sThreadPool.shutdown();
    }
}
