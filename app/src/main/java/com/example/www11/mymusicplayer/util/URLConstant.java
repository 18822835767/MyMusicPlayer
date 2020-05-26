package com.example.www11.mymusicplayer.util;

/**
 * 存放url以及host等信息的常量类.
 * */
public class URLConstant {
    //todo phone是ket 参数是value,如何更优雅地处理
    //服务器ip地址
    private static final String SERVER_HOST = "http://182.254.170.97:3000/";
    //登陆对应的url
    public static final String LOGIN_URL = SERVER_HOST+"login/cellphone?phone=%1$s&password=%2$s";
    //获取轮播图的url
    public static final String BANNER_URL = SERVER_HOST+"banner?type=1";
    //根据歌单id获取歌单中的歌曲
    public static final String MUSIC_INFO_BY_ID_URL = SERVER_HOST+"playlist/detail?id=%s";
    //根据歌曲id获取音乐播放的URL
    public static final String MUSIC_PLAY_URL = SERVER_HOST+"song/url?id=%s";
    //通过歌曲的名字获得歌曲的信息
    public static final String MUSIC_INFO_BY_NAME_URL = SERVER_HOST+"search?keywords= %1$s&lim" +
            "it= %2$s&offset= %3$s";
    public static final String SONG_LIST_URL = SERVER_HOST+"user/playlist?uid=%s";
    //可以在图片url后面加上需要的宽度和高度，即可以加载缩略图,参数是：宽度和高度
    public static final String IMAGE_URL_PARAMS = "?param=%1$sy%2$s";
}
