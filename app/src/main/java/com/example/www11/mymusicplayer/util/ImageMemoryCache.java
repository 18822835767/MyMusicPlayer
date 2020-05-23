package com.example.www11.mymusicplayer.util;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 内存中缓存图片的类.
 * */
public class ImageMemoryCache {
    private static LruCache<String, BitmapDrawable> mMemoryCache;
    
    static {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize  =maxMemory/8;
        mMemoryCache = new LruCache<String,BitmapDrawable>(cacheSize){
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }
    
    public static void addBitmapToMemory(String key,BitmapDrawable drawable){
        if(getBitmapFromMemoryCache(key) == null){
            mMemoryCache.put(key,drawable);
        }
    }
    
    public static BitmapDrawable getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }
    
    
    

}
