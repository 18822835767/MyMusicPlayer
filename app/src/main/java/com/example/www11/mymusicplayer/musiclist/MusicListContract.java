package com.example.www11.mymusicplayer.musiclist;

import java.util.List;
import com.example.www11.mymusicplayer.entity.Music;

public interface MusicListContract {
    /**
     * MusicPresenter接口，被MusicFragment调用.
     * */
    interface Presenter {
        void getMusicList(long songListId);
    }

    /**
     * MusicPresenter将结果反馈给MusicActivity的View接口.
     * 由MusicFragment去实现这个类.
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
        void showError();
    }
}
