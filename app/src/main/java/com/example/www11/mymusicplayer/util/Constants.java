package com.example.www11.mymusicplayer.util;

/**
 * 统一管理常量的类.
 * */
public class Constants {
    private Constants(){}

    /**
     * 管理网络接口的地址和服务器地址.
     * */
    public static class URLConstant{
        //服务器ip地址
        private static final String SERVER_HOST = "http://182.254.170.97:3000/";
        //登陆对应的url
        public static final String LOGIN_URL = SERVER_HOST+"login/cellphone?phone=%1$s&password=%2$s";
        //获取轮播图的url
        public static final String BANNER_URL = SERVER_HOST+"banner?type=1";
        //根据歌单id获取歌单中的歌曲
        public static final String MUSIC_INFO_BY_ID_URL = "http://182.254.170.97:3000/playlist/detail?id=%s";
        //根据歌曲id获取音乐播放的URL
        public static final String MUSIC_PLAY_URL = SERVER_HOST+"song/url?id=%s";
        //通过歌曲的名字获得歌曲的信息
        public static final String MUSIC_INFO_BY_NAME_URL = SERVER_HOST+"search?keywords= %1$s&lim" +
                "it= %2$s&offset= %3$s";
        public static final String SONG_LIST_URL = SERVER_HOST+"user/playlist?uid=%s";
    }
    
    public static class Banner{
        public static final int START = 10;
        public static final int STOP = 20;
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
                "https://p1.music.126.net/u-ZZ94osLvam_mFiVYOwBQ==/106652627911530.jpg"
        };

        //网络请求的访问状态
        public static final int SEARCH_SUCCESS = 0;
        public static final int SEARCH_FAIL = 1;
        public static final int SEARCH_ERROR = 2;
        
        //加载更多歌曲对应的常量
        public static final int LOAD_SUCCESS = 10;
    }
}