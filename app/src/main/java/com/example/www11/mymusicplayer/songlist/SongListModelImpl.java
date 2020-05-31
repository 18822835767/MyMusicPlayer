package com.example.www11.mymusicplayer.songlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.www11.mymusicplayer.songlist.SongListContract;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.IMAGE_HEIGHT;
import static com.example.www11.mymusicplayer.util.Constants.SongListConstant.IMAGE_WIDTH;
import static com.example.www11.mymusicplayer.util.URLConstant.IMAGE_URL_PARAMS;
import static com.example.www11.mymusicplayer.util.URLConstant.SONG_LIST_URL;


public class SongListModelImpl implements SongListModel {
    private List<SongList> mSongLists = new ArrayList<>();

    /**
     * 当用户点击"我的歌单"时，会加载歌单数据.
     */
    @Override
    public void getUserSongList(SongListModel.OnListener onSongListListener, long userId) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(SONG_LIST_URL, userId),
                new HttpUrlConnection.HttpCallbackListener() {
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

    private void handleJson(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);

        JSONArray songListsJson = jsonObject.getJSONArray("playlist");

        for (int i = 0; i < songListsJson.length(); i++) {
            JSONObject songListJson = songListsJson.getJSONObject(i);

            long id = songListJson.getLong("id");
            String name = songListJson.getString("name");
            String coverImgUrl = songListJson.getString("coverImgUrl") +
                    String.format(IMAGE_URL_PARAMS, IMAGE_WIDTH, IMAGE_HEIGHT);

            SongList songList = new SongList(id, name, coverImgUrl);
            mSongLists.add(songList);
        }
    }
}
