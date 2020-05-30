package com.example.www11.mymusicplayer.search;

import com.example.www11.mymusicplayer.search.SearchContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.IMAGE_HEIGHT;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.IMAGE_URLS;
import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.IMAGE_WIDTH;
import static com.example.www11.mymusicplayer.util.URLConstant.IMAGE_URL_PARAMS;
import static com.example.www11.mymusicplayer.util.URLConstant.MUSIC_INFO_BY_NAME_URL;
import static com.example.www11.mymusicplayer.util.URLConstant.MUSIC_PLAY_URL;

public class SearchModelImpl implements SearchModel {
    /**
     * 记录歌曲图片url的下标
     */
    private int index = 0;

    /**
     * 歌曲图片url的数组的长度
     */
    private int length = IMAGE_URLS.length;

    /**
     * 存放搜索到的音乐的list
     */
    private List<Music> mMusics = new ArrayList<>();

    @Override
    public void searchOrLoadMusic(SearchModel.OnListener onSearchListener, String musicName,
                                  int limit, int offset) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(MUSIC_INFO_BY_NAME_URL, musicName, limit,
                offset), new HttpUrlConnection.HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                mMusics.clear();

                try {
                    handleMusicInfoJSON(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (Music music : mMusics) {
                    setMusicUrl(music);
                }

                if (offset == 0) {
                    //偏移量为0，说明是搜索歌曲
                    int songCount = 0;

                    try {
                        songCount = handleSongCount(dataMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    onSearchListener.onSuccess(songCount, mMusics);
                } else {
                    //偏移量不为0，说明是加载更多歌曲
                    onSearchListener.loadMoreMusics(mMusics);
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
     * 获得音乐播放的url.
     */
    private void setMusicUrl(Music music) {
        HttpUrlConnection.sendHttpUrlConnection(String.format(MUSIC_PLAY_URL, music.getId()),
                new HttpUrlConnection.HttpCallbackListener() {
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
     * JSON解析得到音乐的信息.
     */
    private void handleMusicInfoJSON(String dataMessage) throws JSONException {
        JSONObject object = new JSONObject(dataMessage);
        JSONObject result = object.getJSONObject("result");
        JSONArray songs = result.getJSONArray("songs");

        for (int i = 0; i < songs.length(); i++) {
            JSONObject songJson = songs.getJSONObject(i);
            JSONArray artists = songJson.getJSONArray("artists");

            //获取歌手名字
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < artists.length(); j++) {
                builder.append(artists.getJSONObject(j).getString("name")).append(" ");
            }

            long id = songJson.getLong("id");
            String songName = songJson.getString("name");
            String singerName = builder.toString();
            String picUrl = IMAGE_URLS[index] + String.format(IMAGE_URL_PARAMS, IMAGE_WIDTH,
                    IMAGE_HEIGHT);

            Music music = new Music(songName, id, picUrl, singerName);
            mMusics.add(music);

            //更新图片url的索引的值
            if (index == length - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    /**
     * JSON解析通过音乐id得到音乐的URL
     */
    private String handleMusicUrlJson(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject musicInfo = data.getJSONObject(0);
        //url不为null则正常获取，为null则返回字符串"null"
        return musicInfo.optString("url");
    }

    /**
     * 返回搜索的歌曲的歌曲总量.
     */
    private int handleSongCount(String dataMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONObject result = jsonObject.getJSONObject("result");

        return result.getInt("songCount");
    }

}
