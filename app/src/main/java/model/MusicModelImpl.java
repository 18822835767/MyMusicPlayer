package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import contract.MusicContract;
import entity.Music;
import util.HttpCallbackListener;
import util.HttpUrlConnection;

public class MusicModelImpl implements MusicContract.MusicModel {
    //根据歌单id获取歌单中的歌曲
    private static final String MUSIC_INFO = "http://182.254.170.97:3000/playlist/detail?id=";
    
    private List<Music> mMusics = new ArrayList<>();
    
    @Override
    public void getMusicList(MusicContract.OnMusicListener onMusicListener, int songListId) {
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_INFO + songListId,
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void handleJson(String dataMessage) throws JSONException {
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
            int id = musicJson.getInt("id");
            String pirUrl = al.getString("picUrl");
            String singerName = builder.toString();
            
            Music music = new Music(name, id, pirUrl,singerName);
            mMusics.add(music);
        }
    }
}
