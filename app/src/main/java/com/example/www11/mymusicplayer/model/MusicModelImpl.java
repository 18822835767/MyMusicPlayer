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

import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.IMAGE_HEIGHT;
import static com.example.www11.mymusicplayer.util.Constants.MusicConstant.IMAGE_WIDTH;
import static com.example.www11.mymusicplayer.util.URLConstant.IMAGE_URL_PARAMS;
import static com.example.www11.mymusicplayer.util.URLConstant.MUSIC_INFO_BY_ID_URL;
import static com.example.www11.mymusicplayer.util.URLConstant.MUSIC_PLAY_URL;


public class MusicModelImpl implements MusicContract.Model {
    /**
     * 存放音乐的list
     * */
    private List<Music> mMusics = new ArrayList<>();
    
    /**
     * 获得歌单中的音乐列表--数据.
     * */
    @Override
    public void getMusicList(MusicContract.OnListener onMusicListener, long songListId) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(MUSIC_INFO_BY_ID_URL,songListId),
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
            public void onError(String errorMsg) {
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
        HttpUrlConnection.sendHttpUrlConnection(String.format(MUSIC_PLAY_URL,music.getId()), 
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
            public void onError(String errorMsg) {

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
     * JSON解析获得音乐的信息.
     * */
    private void handleMusicInfoJson(String dataMessage) throws JSONException {
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
            String pirUrl = al.getString("picUrl") + String.format(IMAGE_URL_PARAMS,IMAGE_WIDTH,
                    IMAGE_HEIGHT);
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
