package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import contract.SongListContract;
import entity.SongList;
import util.HttpCallbackListener;
import util.HttpUrlConnection;

public class SongListModelImpl implements SongListContract.SongListModel{
    private final String SONG_LIST_URL = "http://182.254.170.97:3000/user/playlist?uid=";
    private List<SongList> songLists = new ArrayList<>();
    private final String TAG = "SongListModelImpl";
    
    @Override
    public void getUserSongList(SongListContract.OnSongListListener onSongListListener, int userId) {
        HttpUrlConnection.sendHttpUrlConnection(SONG_LIST_URL+userId,new HttpCallbackListener(){
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                onSongListListener.onSuccess(songLists);
            }

            @Override
            public void onFail() {
                onSongListListener.onFail();
            }

            @Override
            public void onError() {
                onSongListListener.onError();
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

        JSONArray songListsJson = jsonObject.getJSONArray("playlist");

        for(int i=0;i<songListsJson.length();i++){
            JSONObject songListJson = songListsJson.getJSONObject(i);

            int id = songListJson.getInt("id");
            String name = songListJson.getString("name");
            String coverImgUrl = songListJson.getString("coverImgUrl");

            SongList songList = new SongList(id,name,coverImgUrl);
            songLists.add(songList);
        }
    }
}
