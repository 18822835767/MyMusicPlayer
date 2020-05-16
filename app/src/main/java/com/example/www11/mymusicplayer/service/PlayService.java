package com.example.www11.mymusicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.example.www11.mymusicplayer.contract.PlayMusicContract;
import com.example.www11.mymusicplayer.presenter.PlayPresenterImpl;

/**
 * 当音乐在播放状态时，会开启这一个服务.
 * */
public class PlayService extends Service {
    private PlayMusicContract.PlayPresenter mPlayPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        if(mPlayPresenter == null){
            mPlayPresenter = PlayPresenterImpl.getInstance();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the com.example.www11.mymusicplayer.service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 服务终止时，释放mediaplayer相关资源.
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayer mediaPlayer = mPlayPresenter.getMediaPlayer();
        if(null !=mediaPlayer){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mPlayPresenter = null;
    }
}

