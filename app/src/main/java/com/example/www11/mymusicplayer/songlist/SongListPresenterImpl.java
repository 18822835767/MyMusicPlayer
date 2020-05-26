package com.example.www11.mymusicplayer.songlist;

import java.util.List;

import com.example.www11.mymusicplayer.entity.SongList;

public class SongListPresenterImpl implements SongListContract.Presenter,
        SongListModel.OnListener {
    /**
     * model实现类的引用
     * */
    private SongListModel mSongListModel;
    
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
