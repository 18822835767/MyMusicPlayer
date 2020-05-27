package com.example.www11.mymusicplayer.homepage;

import android.graphics.drawable.Drawable;
import java.util.List;

public interface HomePageContract {
    /**
     * HomePagePresenter接口，被HomePageFragment调用.
     * */
    interface Presenter {
        void getBanner();
    }
    
    interface OnView {
        /**
         * 展示轮播图的图片.
         * */
        void showBanner(List<Drawable> drawableList);

        /**
         * HomePageFragment做开始请求业务的UI处理.
         * */
        void showLoading();

        /**
         * HomePageFragment做结束请求业务的UI处理.
         * */
        void hideLoading();
    }
    
}
