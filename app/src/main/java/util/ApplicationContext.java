package util;

import android.app.Application;
import android.content.Context;

/**
 * 全局获取Context.
 * */
//todo 这玩意还没在manifest里注册咧
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
