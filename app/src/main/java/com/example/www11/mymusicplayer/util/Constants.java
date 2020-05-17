package com.example.www11.mymusicplayer.util;

/**
 * 统一管理常量的类.
 * */
public class Constants {
    private Constants(){}

    public static class PlayMusicConstant{
        //请求音乐播放时所对应的值，播放成功，播放失败，播放错误
        public static final int ERROR = 0;
        public static final int SUCCESS = 1;
        public static final int FAIL = 2;

        //不同的播放模式
        public static final int ORDER_PLAY = 0;//列表循环播放
        public static final int RANDOM_PLAY = 1;//随机播放
        public static final int LOOP_PLAY = 2;//单曲循环

        //表示播放状态
        public static final int PLAY_STATE_PLAY = 1;//播放
        public static final int PLAY_STATE_PAUSE = 2;//暂停
        public static final int PLAY_STATE_STOP = 3;//停止
    }
}