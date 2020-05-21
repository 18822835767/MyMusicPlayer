package com.example.www11.mymusicplayer.contract;

import android.widget.ImageView;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

public interface HomePageContract {
    /**
     * todo 一般命名为Model就行了，调用时HomePageContract.Model，包括下面的View，Presenter
     * HomePageModel接口，被HomePagePresenter调用.
     * */
    interface HomePageModel{
        void getBanner(HomePageContract.OnHomePageListener onHomePageListener);
    }

    /**
     * HomePagePresenter接口，被HomePageFragment调用.
     * */
    interface HomePagePresenter{
        void getBanner();
    }
    
    
    interface OnHomePageListener{
        /**
         * 成功得到轮播图的图片.
         * */
        void onSuccess(List<ImageView> imageList);
    }
    
    interface OnHomePageView{
        /**
         * 展示轮播图的图片.
         * */
        void showBanner(List<ImageView> imageList);
    }
    
}
