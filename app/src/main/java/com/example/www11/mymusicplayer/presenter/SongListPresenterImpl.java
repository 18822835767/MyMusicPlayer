package com.example.www11.mymusicplayer.presenter;

import java.util.List;

import com.example.www11.mymusicplayer.contract.SongListContract;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.model.SongListModelImpl;

public class SongListPresenterImpl implements SongListContract.SongListPresenter,
        SongListContract.OnSongListListener {
    private SongListContract.SongListModel mSongListModel;//model实现类的引用
    private SongListContract.OnSongListView mOnSongListView;//view接口的引用
    
    public SongListPresenterImpl(SongListContract.OnSongListView onSongListView){
        this.mOnSongListView = onSongListView;
        mSongListModel = new SongListModelImpl();
    }
    
    @Override
    public void getUserSongList(long id) {
        mSongListModel.getUserSongList(this,id);
    }

    @Override
    public void onSuccess(List<SongList> songLists) {
        mOnSongListView.showSongList(songLists);        
    }

    @Override
    public void onFail() {
        mOnSongListView.showFail();
    }

    @Override
    public void onError() {
        mOnSongListView.showError();
    }
    
}
