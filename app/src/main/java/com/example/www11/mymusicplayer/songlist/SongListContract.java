package com.example.www11.mymusicplayer.songlist;

import org.json.JSONException;

import java.util.List;

import com.example.www11.mymusicplayer.entity.SongList;

public interface SongListContract {
    /**
     * SongListPresenter接口，被SongListFragment调用.
     * */
    interface Presenter {
        void getUserSongList(long userId);
    }
    
    /**
     * Presenter将结果反馈给Fragment的View接口.
     * 由Fragment去实现这个类.
     * */
    interface OnView {
        /**
         * 展示用户歌单.
         * */
        void showSongList(List<SongList> songLists);

        /**
         * 没能成功得到歌单.
         * */
        void showFail();
        
        /**
         * 错误(断网等情况...).
         * */
        void showError(String errorMsg);
    }
}
