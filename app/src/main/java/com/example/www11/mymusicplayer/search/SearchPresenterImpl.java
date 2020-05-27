package com.example.www11.mymusicplayer.search;

import com.example.www11.mymusicplayer.entity.Music;

import java.util.List;

public class SearchPresenterImpl implements SearchModel.OnListener,
        SearchContract.Presenter {
    private SearchContract.OnView mOnSearchView;
    private SearchModel mSearchModel;
    
    SearchPresenterImpl(SearchContract.OnView onSearchView){
        mOnSearchView = onSearchView;
        mSearchModel = new SearchModelImpl();
    }
    
    @Override
    public void searchOrLoadMusic(String musicName, int limit, int offset) {
        mSearchModel.searchOrLoadMusic(this,musicName,limit,offset);
    }
    
    @Override
    public void onSuccess(int songCount,List<Music> musics) {
        mOnSearchView.showSearchMusics(songCount,musics);
    }

    @Override
    public void loadMoreMusics(List<Music> musics) {
        mOnSearchView.loadMoreMusics(musics);
    }
}
