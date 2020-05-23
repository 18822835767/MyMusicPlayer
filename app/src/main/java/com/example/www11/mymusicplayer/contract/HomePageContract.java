package com.example.www11.mymusicplayer.contract;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

public interface HomePageContract {
    /**
     * HomePageModel接口，被HomePagePresenter调用.
     * */
    interface Model {
        void getBanner(OnListener onHomePageListener);
    }

    /**
     * HomePagePresenter接口，被HomePageFragment调用.
     * */
    interface Presenter {
        void getBanner();
    }
    
    
    interface OnListener {
        /**
         * 成功得到轮播图的图片.
         * */
        void onSuccess(List<Drawable> drawableList);
    }
    
    interface OnView {
        /**
         * 展示轮播图的图片.
         * */
        void showBanner(List<Drawable> drawableList);
    }
    
}
