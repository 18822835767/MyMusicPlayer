package com.example.www11.mymusicplayer.contract;

import com.example.www11.mymusicplayer.entity.Music;

import org.json.JSONException;

import java.util.List;

/**
 * 搜索相关操作所对应的契约类.
 * */
public interface SearchContract {
    /**
     * SearchModel接口，被SearchPresenter调用.
     * */
    interface SearchModel{
        void searchMusic(OnSearchListener onSearchListener,String musicName,int limit,int offset);
    }
    
    /**
     * SearchPresenter接口，被SearchFragment调用.
     * */
    interface SearchPresenter{
        void searchMusic(String musicName,int limit,int offset);
    }
    
    /**
     * 这是将SearchModel请求结果反馈给SearchPresenter的Callback接口.
     * SearchPresenter要去实现这个接口.
     * */
    interface OnSearchListener{
        void onSuccess(List<Music> musics);
    }

    /**
     * SearchPresenter将登陆结果反馈给SearchFragment的View接口.
     * 由SearchFragment去实现这个类.
     * */
    interface OnSearchView{
        /**
         * 展示用户搜索得到的歌曲.
         * */
        void showSearchMusics(List<Music> musics);
        
        /**
         * 上拉刷新的时候调用，展示更多的歌曲.
         * */
        void loadMoreMusics(List<Music> musics);
    }
}
