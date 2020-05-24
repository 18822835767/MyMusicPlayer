package com.example.www11.mymusicplayer.util;

import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;

/**
 * 内存中缓存图片的类.
 * */
public class ImageMemoryCache {
    /**
     * 缓存图片，会将缓存好后的图片放在这里，在图片占用内存达到设定的最大值时，会根据算法移除最近最少使用的图片.
     * */
    private static LruCache<String, BitmapDrawable> sMemoryCache;
    
    static {
        //应用程序最大的可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //部分作为图片缓存
        int cacheSize  =maxMemory/8;
        sMemoryCache = new LruCache<String,BitmapDrawable>(cacheSize){
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();//返回图片所占字节数
            }
        };
    }
    
    /**
     * 想缓存中加入图片.
     * */
    public static void addBitmapToMemory(String key,BitmapDrawable drawable){
        if(getBitmapFromMemoryCache(key) == null){
            sMemoryCache.put(key,drawable);
        }
    }
    
    /**
     * 从缓存中得到图片.
     * @return 有相应的图片则返回，没有则返回null
     * */
    public static BitmapDrawable getBitmapFromMemoryCache(String key){
        return sMemoryCache.get(key);
    }
}
