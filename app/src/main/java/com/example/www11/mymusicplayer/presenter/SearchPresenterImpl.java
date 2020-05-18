package com.example.www11.mymusicplayer.presenter;

import com.example.www11.mymusicplayer.contract.SearchContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.model.SearchModelImpl;

import java.util.List;

public class SearchPresenterImpl implements SearchContract.OnSearchListener,
        SearchContract.SearchPresenter {
    private SearchContract.OnSearchView mOnSearchView;
    private SearchContract.SearchModel mSearchModel;
    
    public SearchPresenterImpl(SearchContract.OnSearchView onSearchView){
        mOnSearchView = onSearchView;
        mSearchModel = new SearchModelImpl();
    }
    
    @Override
    public void searchMusic(String musicName,int limit,int offset) {
        mSearchModel.searchMusic(this,musicName,limit,offset);
    }

    @Override
    public void onSuccess(List<Music> musics) {
        mOnSearchView.showSearchMusics(musics);
    }
}
