package com.example.www11.mymusicplayer.songlist;

import com.example.www11.mymusicplayer.entity.SongList;

import org.json.JSONException;

import java.util.List;

public interface SongListModel {
    /**
     * SongListModel接口.
     */
    void getUserSongList(OnListener onSongListListener, long userId);
    
    /**
     * 这是将SongListModel请求结果反馈给SongListPresenter的Callback接口.
     * SongListPresenter要去实现这个接口.
     */
    interface OnListener {
        /**
         * 成功得到歌单.
         */
        void onSuccess(List<SongList> songLists);

        /**
         * 没能成功得到歌单.
         */
        void onFail();

        /**
         * 错误(断网...).
         */
        void onError(String errorMsg);
    }
}
