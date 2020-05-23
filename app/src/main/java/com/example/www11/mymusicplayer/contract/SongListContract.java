package com.example.www11.mymusicplayer.contract;

import org.json.JSONException;

import java.util.List;

import com.example.www11.mymusicplayer.entity.SongList;

public interface SongListContract {
    /**
     * SongListModel接口，被SongListPresenter调用.
     * */
    interface Model {
       void getUserSongList(OnListener onSongListListener, long userId);
       void handleJson(String dataMessage) throws JSONException;
    }
    
    /**
     * SongListPresenter接口，被SongListFragment调用.
     * */
    interface Presenter {
        void getUserSongList(long userId);
    }
    
    /**
     * 这是将SongListModel请求结果反馈给SongListPresenter的Callback接口.
     * SongListPresenter要去实现这个接口.
     * */
    interface OnListener {
        /**
         * 成功得到歌单.
         * */
        void onSuccess(List<SongList> songLists);
        
        /**
         * 没能成功得到歌单.
         * */
        void onFail();
        
        /**
         * 错误(断网...).
         * */
        void onError(String errorMsg);
    }
    
    /**
     * SongListPresenter将结果反馈给SongListActivity的View接口.
     * 由SongListFragment去实现这个类.
     * */
    interface OnView {
        /**
         * 展示用户歌单.
         * */
        void showSongList(List<SongList> songLists);

        /**
         * 没能成功得到歌单.
         * */
        void showFail();
        
        /**
         * 错误(断网等情况...).
         * */
        void showError(String errorMsg);
    }
}
