package com.example.www11.mymusicplayer.util;

import android.app.Application;
import android.content.Context;

/**
 * 全局获取Context.
 * */
public class ApplicationContext extends Application {
    private static Context sContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
    
    public static Context getContext(){
        return sContext;
    }
}
