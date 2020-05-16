package com.example.www11.mymusicplayer.util;

import android.app.Application;
import android.content.Context;

/**
 * 全局获取Context.
 * */
public class ApplicationContext extends Application {
    private static Context mContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
    
    public static Context getContext(){
        return mContext;
    }
}
