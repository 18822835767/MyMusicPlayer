package com.example.www11.mymusicplayer.presenter;

import java.util.List;

import com.example.www11.mymusicplayer.contract.MusicContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.model.MusicModelImpl;

public class MusicPresenterImpl implements MusicContract.MusicPresenter, 
        MusicContract.OnMusicListener {
    private MusicContract.MusicModel mMusicModel;
    private MusicContract.OnMusicView mOnMusicView;

    public MusicPresenterImpl(MusicContract.OnMusicView onMusicView){
        this.mOnMusicView = onMusicView;
        mMusicModel = new MusicModelImpl();
    }
    
    @Override
    public void getMusicList(long songListId) {
        mMusicModel.getMusicList(this,songListId);
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
    public void onError() {
        mOnMusicView.showError();
    }
}
