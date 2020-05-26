package com.example.www11.mymusicplayer.search;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

/**
 * 搜索相关操作所对应的契约类.
 * */
public interface SearchContract {
    /**
     * SearchPresenter接口，被SearchFragment调用.
     * */
    interface Presenter {
        void searchOrLoadMusic(String musicName, int limit, int offset);//搜索歌曲
    }

    /**
     * SearchPresenter将登陆结果反馈给SearchFragment的View接口.
     * 由SearchFragment去实现这个类.
     * */
    interface OnView {
        /**
         * 展示用户搜索得到的歌曲.
         * */
        void showSearchMusics(int songCount,List<Music> musics);
        
        /**
         * 上拉刷新的时候调用，展示更多的歌曲.
         * */
        void loadMoreMusics(List<Music> musics);
    }
}
