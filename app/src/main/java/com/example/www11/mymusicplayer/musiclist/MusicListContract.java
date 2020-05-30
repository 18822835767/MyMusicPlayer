package com.example.www11.mymusicplayer.musiclist;

import java.util.List;
import com.example.www11.mymusicplayer.entity.Music;

public interface MusicListContract {
    /**
     * Presenter接口，被Fragment调用.
     * */
    interface Presenter {
        void getMusicList(long songListId);
    }

    /**
     * Presenter将结果反馈给Fragment的View接口.
     * 由Fragment去实现这个类.
     * */
    interface OnView {
        /**
         * 展示用户歌单.
         * */
        void showMusics(List<Music> music);

        /**
         * 没能成功得到歌单中的歌曲.
         * */
        void showFail();
        
        /**
         * 错误(断网等情况...).
         * */
        void showError(String errorMsg);
    }
}
