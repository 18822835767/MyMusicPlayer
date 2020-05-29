package com.example.www11.mymusicplayer.util;

/**
 * 统一管理常量的类.
 * */
public class Constants {
    private Constants(){}
    
    /**
     * 轮播图控件对应的常量.
     * */
    public static class Banner{
        public static final int START = 10;
        public static final int STOP = 20;
    }
    
    /**
     * 音乐首页对应的常量.
     * */
    public static class HomePage{
        public static final int ERROR = 0;
    }
    
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

        //歌曲列表图片大小对应的常量
        public static final int IMAGE_WIDTH = 50;
        public static final int IMAGE_HEIGHT = 50;
    }
    
    /**
     * 歌单的音乐列表对应的常量.
     * */
    public static class MusicConstant{
        //网络请求的访问状态
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int ERROR = 2;
        
        //歌曲列表图片大小对应的常量
        public static final int IMAGE_WIDTH = 50;
        public static final int IMAGE_HEIGHT = 50;
    }
    
    /**
     * 登陆对应的常量.
     * */
    public static class LoginConstant{
        public static final String USER = "user";
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int ERROR = 2;
        public static final int SHOW_LOADING = 3;
        public static final int HIDE_LOADING = 4;
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
    
    public static class ServiceConstant{
        //前台服务的Channel的Id
        public static final String CHANNEL_ID = "10";
        //前台服务的Channel的名字
        public static final String CHANNEL_NAME = "音乐状态栏";
        
    }
    
    /**
     * 搜索对应的常量值.
     * */
    public static class SearchConstant{
        //放着歌曲url常量
        public static final String [] IMAGE_URLS = {
                "https://p2.music.126.net/636SSPpKW0avAqkK1QgzgQ==/43980465112096.jpg",
                "https://p2.music.126.net/g86i59ugEFw2FgMXHtycUw==/109951164948362656.jpg",
                "https://p1.music.126.net/g8Jei79i42iO_TohHR8_4g==/74766790706376.jpg",
                "https://p1.music.126.net/ER31PgQ7xZTC7EV4IGwAaw==/54975581405893.jpg",
                "https://p2.music.126.net/Bl1hEdJbMSj5YJsTqUjr-w==/109951163520311175.jpg",
                "https://p2.music.126.net/QFFC8k23113qcETrZP8cUg==/6638851209508131.jpg",
                "https://p2.music.126.net/v0LaYOX44ZiJjC1bzZbKOg==/109951163884032084.jpg",
                "https://p1.music.126.net/SVVLu19lyd5-2fAkZUzJaA==/95657511628586.jpg",
                "https://p1.music.126.net/z6RjWF1-xzHCSPsGJdCYzg==/84662395356387.jpg",
                "https://p1.music.126.net/u-ZZ94osLvam_mFiVYOwBQ==/106652627911530.jpg",
                
                "https://p1.music.126.net/PcJq6i7zMcPgudejdn1yeg==/109951163256302356.jpg",
                "https://p2.music.126.net/08gFETjPzObLUSTgaNr1gg==/109951163855320167.jpg",
                "https://p2.music.126.net/wCTWFXLFu-_iCvyUpz3MgA==/556352883666649.jpg",
                "https://p2.music.126.net/b2nl6jsVbqj23IV8dVvJcg==/7766950139663735.jpg",
                "https://p2.music.126.net/n9D1R_zL_XaKW9zfW8s2tA==/5920870115754269.jpg",
                "https://p2.music.126.net/DmtHv4WgoAUWRNFcaNJGlA==/3395291908778307.jpg",
                "https://p2.music.126.net/PehnaQ-BaAXeQg1d5s7leA==/6635552673699715.jpg",
                "https://p1.music.126.net/s8V5kdNiwyEQb0nrVuh-PQ==/109951164713484351.jpg",
                "https://p2.music.126.net/wGhf8z-_BYki8uPBHmWgZA==/109951163463758536.jpg",
                "https://p2.music.126.net/dLkalW4CyC6hAZQS6xXFeA==/109951163649564629.jpg"
        };

        //网络请求的访问状态
        public static final int SEARCH_SUCCESS = 0;
        public static final int SEARCH_FAIL = 1;
        public static final int SEARCH_ERROR = 2;
        
        //加载更多歌曲对应的常量
        public static final int LOAD_SUCCESS = 10;

        //歌曲列表图片大小对应的常量
        public static final int IMAGE_WIDTH = 50;
        public static final int IMAGE_HEIGHT = 50;
    }
}