package model;

import android.util.Log;

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
    private final String MUSIC_URL = "http://182.254.170.97:3000/playlist/detail?id=";
    private List<Music> musics = new ArrayList<>();
    
    @Override
    public void getMusicList(MusicContract.OnMusicListener onMusicListener, int songListId) {
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_URL + songListId,
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }
                
                onMusicListener.onSuccess(musics);
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
    public void handleJson(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONObject playList = jsonObject.getJSONObject("playlist");
        JSONArray tracks = playList.getJSONArray("tracks");

        for (int i = 0; i < tracks.length(); i++) {
            JSONObject musicJson = tracks.getJSONObject(i);
            JSONObject al = musicJson.getJSONObject("al");

            String name = musicJson.getString("name");
            int id = musicJson.getInt("id");
            String pirUrl = al.getString("picUrl");

            Music music = new Music(name, id, pirUrl);
            musics.add(music);
        }
    }
}
