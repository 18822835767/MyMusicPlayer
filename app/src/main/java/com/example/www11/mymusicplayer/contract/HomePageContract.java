package com.example.www11.mymusicplayer.contract;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

public interface HomePageContract {
    /**
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
        void onSuccess(List<String> bannerUrl);
    }
    
    interface OnHomePageView{
        /**
         * 展示轮播图的图片.
         * */
        void showBanner(List<String> bannerUrl);
    }
    
}
