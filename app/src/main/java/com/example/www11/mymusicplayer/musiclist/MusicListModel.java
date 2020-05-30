package com.example.www11.mymusicplayer.musiclist;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

/**
 * MusicModel接口.
 */
public interface MusicListModel {
    void getMusicList(OnListener onMusicListener, long songListId);
        
    /**
     * 这是将Model请求结果反馈给Presenter的Callback接口.
     * Presenter要去实现这个接口.
     */
    interface OnListener {
        /**
         * 成功得到歌单中的歌曲.
         */
        void onSuccess(List<Music> musics);

        /**
         * 没能成功得到歌单中的歌曲.
         */
        void onFail();

        /**
         * 错误(断网...).
         */
        void onError(String errorMsg);
    }
}
