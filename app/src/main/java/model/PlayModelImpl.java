package model;

import java.util.List;

import contract.PlayMusicContract;
import entity.Music;
import util.HttpUrlConnection;

public class PlayModelImpl implements PlayMusicContract.PlayModel {
    //获取音乐播放的URL
    private static final String MUSIC_URL = "http://182.254.170.97:3000/song/url?id=";
    @Override
    public List<String> getMusicsUrl(List<Music> musics) {
        
        return null;
    }
}
