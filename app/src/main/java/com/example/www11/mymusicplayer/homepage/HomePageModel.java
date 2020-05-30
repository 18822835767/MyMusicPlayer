package com.example.www11.mymusicplayer.homepage;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * HomePageModel接口.
 * */
public interface HomePageModel {
    void getBanner(OnListener onHomePageListener);
        
    interface OnListener {
        /**
         * 成功得到轮播图的图片.
         * */
        void onSuccess(List<Drawable> drawableList);

        /**
         * 
         * 加载错误(断网...).
         */
        void onError(String errorMsg);
    }
    
}
