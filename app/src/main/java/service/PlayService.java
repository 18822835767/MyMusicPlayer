package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import contract.PlayMusicContract;
import presenter.PlayPresenterImpl;

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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

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

