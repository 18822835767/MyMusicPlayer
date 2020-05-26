package com.example.www11.mymusicplayer.search;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

/**
 * SearchModel接口.
 */
public interface SearchModel {
    //搜索歌曲或者加载更多歌曲
    void searchOrLoadMusic(OnListener onSearchListener, String musicName, int limit,
                           int offset);

    /**
     * 这是将SearchModel请求结果反馈给SearchPresenter的Callback接口.
     * SearchPresenter要去实现这个接口.
     */
    interface OnListener {
        void onSuccess(int songCount, List<Music> musics);

        void loadMoreMusics(List<Music> musics);
    }
}
