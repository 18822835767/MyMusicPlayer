package com.example.www11.mymusicplayer.util;

/**
 * 统一管理常量的类.
 * */
public class Constants {
    private Constants(){}

    /**
     * 底部播放栏对应的常量.
     * */
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
    
    /**
     * 歌单列表对应的常量.
     * */
    public static class SongListConstant{
        //网络请求的访问状态
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int ERROR = 2;
    }
    
    /**
     * 歌单的音乐列表对应的常量.
     * */
    public static class MusicConstant{
        //网络请求的访问状态
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int ERROR = 2;
    }
    
    /**
     * 登陆对应的常量.
     * */
    public static class LoginConstant{
        public static final String USER = "user";
    }
    
    /**
     * 主活动所对应的常量.
     * */
    public static class MainConstant{
        //碎片所对应的常量值
        public static final int SHOW_HOME_PAGE = 0;
        public static final int SHOW_SONG_LIST = 1;
        public static final int SHOW_MUSIC = 2;
        public static final int SHOW_SEARCH = 3;

        //权限请求码
        public static final int REQUEST_CODE = 50;
    }
    
    /**
     * 搜索对应的常量值.
     * */
    public static class SearchConstant{
        //放着歌曲url常量
        public static final String [] IMAGE_URLS = {
                "https://p2.music.126.net/636SSPpKW0avAqkK1QgzgQ==/43980465112096.jpg",
                "https://p2.music.126.net/g86i59ugEFw2FgMXHtycUw==/109951164948362656.jpg",
                "https://p2.music.126.net/stFBHt_dgFHAxvu8XPJpsg==/1699844976542443.jpg",
                "https://p2.music.126.net/ghmbmEQS-IJfZPjdA3KGxg==/82463372084291.jpg",
                "https://p1.music.126.net/JOJvZc_7SqQjKf8TktQ_bw==/29686813951246.jpg",
                "https://p2.music.126.net/ZJvsIcd51VAZx3-YuEAcFQ==/18612532836965764.jpg",
                "https://p2.music.126.net/v0LaYOX44ZiJjC1bzZbKOg==/109951163884032084.jpg",
                "https://p1.music.126.net/SVVLu19lyd5-2fAkZUzJaA==/95657511628586.jpg",
                "https://p1.music.126.net/z6RjWF1-xzHCSPsGJdCYzg==/84662395356387.jpg",
                "https://p1.music.126.net/u-ZZ94osLvam_mFiVYOwBQ==/106652627911530.jpg"
        };
    }
}