package com.example.www11.mymusicplayer.contract;

import android.media.MediaPlayer;

import java.util.List;

import com.example.www11.mymusicplayer.entity.Music;

//todo emmm实际上这里属于音乐控制的部分，并不用以mvp的形式进行设计
public interface PlayMusicContract {
    /**
     * 这是PlayPresenter接口，给PlayMusicFragment调用.
     * */
    interface Presenter {
        /**
         * 控制音乐的播放或者暂停
         * */
        void playOrPause();
        
        /**
         * 控制音乐的播放进度
         * */
        void seekTo(int seek);
        
        void registOnPlayView(OnView onPlayView);
       
        MediaPlayer getMediaPlayer();
        
        /**
         * 播放下一首.
         * */
        void playNext();
        
        /**
         * 播放上一首.
         * */
        void playPre();
        
        /**
         * 用户点播音乐时,传入歌所在的列表,以及该歌的位置
         * */
        void playMusic(List<Music> musics,int position);
        
        /**
         * 改变音乐的播放状态
         * */
        void changePlayMode(int mode);
    }

    /**
     * 这是playPresenter实现类将结果反馈给PlayMusicFragment的view接口，由PlayMusicFragment实现，
     * 达到更新UI的目的.
     * */
    interface OnView {
        /**
         * 播放状态改变了，通知view层更新UI。
         * */
        void onPlayStateChange(int state);
        
        /**
         * 通知view层更新进度条UI.
         * */
        void onSeekChange(int seek);
        
        /**
         * 展示歌曲的信息：专辑图片，歌手名字，歌名.
         * */
        void showMusicInfo(Music music);
        
        /**
         * 播放出现错误时给用户提示.
         * */
        void showError();
        
        void showFail(String msg);
    }
}
