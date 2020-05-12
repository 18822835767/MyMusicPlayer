package service;

import android.app.Service;
import android.content.Intent;
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
        playPresenter = null;
    }
}

