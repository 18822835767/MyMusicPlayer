package com.example.www11.mymusicplayer.musiclist;

import java.util.List;

import com.example.www11.mymusicplayer.entity.Music;

public class MusicListPresenterImpl implements MusicListContract.Presenter,
        MusicListModel.OnListener {
    private MusicListModel mMusicModel;
    private MusicListContract.OnView mOnMusicView;

    MusicListPresenterImpl(MusicListContract.OnView onMusicView) {
        this.mOnMusicView = onMusicView;
        mMusicModel = new MusicListModelImpl();
    }
    
    @Override
    public void getMusicList(long songListId) {
        mMusicModel.getMusicList(this, songListId);
    }

    @Override
    public void onSuccess(List<Music> musics) {
        mOnMusicView.showMusics(musics);
    }

    @Override
    public void onFail() {
        mOnMusicView.showFail();
    }

    @Override
    public void onError(String errorMsg) {
        mOnMusicView.showError(errorMsg);
    }
}
