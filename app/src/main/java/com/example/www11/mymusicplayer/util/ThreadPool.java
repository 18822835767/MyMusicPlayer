package com.example.www11.mymusicplayer.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池.
 * */
public class ThreadPool {
    private static ExecutorService threadPool;
    
    static {
        threadPool = Executors.newCachedThreadPool();
    }
    
    static ExecutorService getThreadPool(){
        return threadPool;
    }
    
    public static void shutDownPool(){
        threadPool.shutdown();
    }
}
