package com.example.www11.mymusicplayer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.www11.mymusicplayer.contract.SongListContract;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import static com.example.www11.mymusicplayer.util.Constants.URLConstant.SONG_LIST_URL;

public class SongListModelImpl implements SongListContract.SongListModel{
    private List<SongList> mSongLists = new ArrayList<>();
    
    /**
     * 当用户点击"我的歌单"时，会加载歌单数据.
     * */
    @Override
    public void getUserSongList(SongListContract.OnSongListListener onSongListListener, long userId) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(SONG_LIST_URL,userId),new HttpCallbackListener(){
            @Override
            public void onSuccess(String dataMessage) {
                try {
                    handleJson(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                onSongListListener.onSuccess(mSongLists);
            }

            @Override
            public void onFail() {
                onSongListListener.onFail();
            }

            @Override
            public void onError(String errorMsg) {
                onSongListListener.onError(errorMsg);
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

            long id = songListJson.getLong("id");
            String name = songListJson.getString("name");
            String coverImgUrl = songListJson.getString("coverImgUrl");
            
            SongList songList = new SongList(id,name,coverImgUrl);
            mSongLists.add(songList);
        }
    }
}
