package com.example.www11.mymusicplayer.playmusic;

import com.example.www11.mymusicplayer.entity.Music;

/**
 * 回调接口，音乐播放控制器通知碎片改变UI.
 * */
public interface OnView {
    /**
     * 播放状态改变了，通知view层更新UI。
     */
    void onPlayStateChange(int state);

    /**
     * 通知view层更新进度条UI.
     */
    void onSeekChange(int seek);

    /**
     * 展示歌曲的信息：专辑图片，歌手名字，歌名.
     */
    void showMusicInfo(Music music);

    /**
     * 播放出现错误时给用户提示.
     */
    void showError();

    void showFail(String msg);

    /**
     * 通知播放队列更新数据.
     */
    void changeDialogData();

    /**
     * 展示音乐播放模式的UI.
     */
    void showPlayMode(int playMode);
}
