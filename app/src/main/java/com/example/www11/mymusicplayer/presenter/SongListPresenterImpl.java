package com.example.www11.mymusicplayer.presenter;

import java.util.List;

import com.example.www11.mymusicplayer.contract.SongListContract;
import com.example.www11.mymusicplayer.entity.SongList;
import com.example.www11.mymusicplayer.model.SongListModelImpl;

public class SongListPresenterImpl implements SongListContract.Presenter,
        SongListContract.OnListener {
    /**
     * model实现类的引用
     * */
    private SongListContract.Model mSongListModel;
    
    /**
     * view接口的引用
     * */
    private SongListContract.OnView mOnSongListView;
    
    public SongListPresenterImpl(SongListContract.OnView onSongListView){
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
    public void onError(String errorMsg) {
        mOnSongListView.showError(errorMsg);
    }
    
}
