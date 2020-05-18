package com.example.www11.mymusicplayer.model;

import com.example.www11.mymusicplayer.contract.SearchContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.HttpCallbackListener;
import com.example.www11.mymusicplayer.util.HttpUrlConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.www11.mymusicplayer.util.Constants.SearchConstant.IMAGE_URLS;

public class SearchModelImpl implements SearchContract.SearchModel {
    private int index = 0;//记录歌曲图片url的下标
    private int length = IMAGE_URLS.length;//歌曲图片url的数组的长度
    //通过歌曲的名字获得歌曲的信息
    private static final String MUSIC_INFO = "http://182.254.170.97:3000/search?keywords= ";
    //根据歌曲id获取音乐播放的URL
    private static final String MUSIC_PLAY_URL = "http://182.254.170.97:3000/song/url?id=";
    
    private List<Music> mMusics = new ArrayList<>();//存放搜索到的音乐的list
    
    @Override
    public void searchMusic(SearchContract.OnSearchListener onSearchListener, String musicName,
            int limit,int offset) {
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_INFO + musicName+"&limit= "+
                        limit+"&offset= "+offset, 
                new HttpCallbackListener() {
            @Override
            public void onSuccess(String dataMessage) {
                mMusics.clear();
                
                try {
                    handleMusicInfoJSON(dataMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(Music music : mMusics){
                    setMusicUrl(music);
                }
                
                onSearchListener.onSuccess(mMusics);
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

    /**
     * 获得音乐播放的url.
     * */
    private void setMusicUrl(Music music){
        HttpUrlConnection.sendHttpUrlConnection(MUSIC_PLAY_URL + music.getId(),
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
    
    /**
     * JSON解析得到音乐的信息.
     * */
    private void handleMusicInfoJSON(String dataMessage) throws JSONException {
        JSONObject object = new JSONObject(dataMessage);
        JSONObject result = object.getJSONObject("result");
        JSONArray songs = result.getJSONArray("songs");
        
        for(int i=0;i<songs.length();i++){
            JSONObject songJson = songs.getJSONObject(i);
            JSONArray artists = songJson.getJSONArray("artists");
            
            //获取歌手名字
            StringBuilder builder = new StringBuilder();
            for(int j=0;j<artists.length();j++){
                builder.append(artists.getJSONObject(j).getString("name")).append(" ");
            }
            
            long id = songJson.getLong("id");
            String songName = songJson.getString("name");
            String singerName = builder.toString();
            String picUrl = IMAGE_URLS[index];
            
            Music music = new Music(songName,id,picUrl,singerName);
            mMusics.add(music);
            
            //更新图片url的索引的值
            if(index == length-1){
                index = 0;
            }else{
                index++;
            }
        }
    }

    /**
     * JSON解析通过音乐id得到音乐的URL
     * */
    private String handleMusicUrlJson(String dataMessage) throws JSONException{
        JSONObject jsonObject = new JSONObject(dataMessage);
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject musicInfo = data.getJSONObject(0);
        //url不为null则正常获取，为null则返回字符串"null"
        return musicInfo.optString("url");
    }
}
