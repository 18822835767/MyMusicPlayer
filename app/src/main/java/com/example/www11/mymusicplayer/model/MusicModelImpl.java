package com.example.www11.mymusicplayer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.www11.mymusicplayer.contract.MusicContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

public class MusicModelImpl implements MusicContract.MusicModel {
    //根据歌单id获取歌单中的歌曲
    private static final String MUSIC_INFO = "http://182.254.170.97:3000/playlist/detail?id=";
    //根据歌曲id获取音乐播放的URL
    private static final String MUSIC_URL = "http://182.254.170.97:3000/song/url?id=";
    //存放音乐的list
    private List<Music> mMusics = new ArrayList<>();
    
    /**
     * 获得歌单中的音乐列表--数据.
     * */
    @Override
    public void getMusicList(MusicContract.OnMusicListener onMusicListener, long songListId) {
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_INFO + songListId,
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleMusicInfoJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
                for(Music music : mMusics){
                    setMusicUrl(music);
                }
                
                onMusicListener.onSuccess(mMusics);
            }

            @Override
            public void onFail() {
                onMusicListener.onFail();
            }

            @Override
            public void onError() {
                onMusicListener.onError();
            }

            @Override
            public void onStart() {
                
            }

            @Override
            public void onFinish() {

            }
        });
    }
    
   /**
    * 获得音乐播放的url.
    * */
    private void setMusicUrl(Music music){
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_URL + music.getId(), 
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    String url = handleMusicUrlJson(dataMessage);
                    music.setMusicURL(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void handleMusicInfoJson(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONObject playList = jsonObject.getJSONObject("playlist");
        JSONArray tracks = playList.getJSONArray("tracks");

        for (int i = 0; i < tracks.length(); i++) {
            JSONObject musicJson = tracks.getJSONObject(i);
            JSONObject al = musicJson.getJSONObject("al");
            JSONArray arArray = musicJson.getJSONArray("ar");
            
            //获取歌手名字
            StringBuilder builder = new StringBuilder();
            for(int j = 0;j<arArray.length();j++){
                builder.append(arArray.getJSONObject(j).getString("name")).append(" ");
            }
            
            //歌名 歌id 专辑URL  歌手名字
            String name = musicJson.getString("name");
            long id = musicJson.getLong("id");
            String pirUrl = al.getString("picUrl");
            String singerName = builder.toString();
            
            Music music = new Music(name, id, pirUrl,singerName);
            mMusics.add(music);
        }
    }
    
    /**
     * JSON解析得到音乐的URL
     * */
    private String handleMusicUrlJson(String dataMessage) throws JSONException{
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject musicInfo = data.getJSONObject(0);
        //url不为null则正常获取，为null则返回字符串"null"
        return musicInfo.optString("url");
    }
}
