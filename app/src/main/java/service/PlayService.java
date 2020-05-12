package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import contract.PlayMusicContract;
import presenter.PlayPresenterImpl;

public class PlayService extends Service {
    private PlayMusicContract.PlayPresenter playPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        if(playPresenter == null){
            playPresenter = PlayPresenterImpl.getInstance();
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
        MediaPlayer mediaPlayer = playPresenter.getMediaPlayer();
        if(null !=mediaPlayer){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playPresenter = null;
    }
}

