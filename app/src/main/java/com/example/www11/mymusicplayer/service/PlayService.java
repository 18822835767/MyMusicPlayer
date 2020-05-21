package com.example.www11.mymusicplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import com.example.mymusicplayer.R;
import com.example.www11.mymusicplayer.contract.PlayMusicContract;
import com.example.www11.mymusicplayer.presenter.PlayPresenterImpl;

import static com.example.www11.mymusicplayer.util.Constants.ServiceConstant.CHANNEL_ID;
import static com.example.www11.mymusicplayer.util.Constants.ServiceConstant.CHANNEL_NAME;

/**
 * 当音乐在播放状态时，会开启这一个服务.
 */
public class PlayService extends Service {
    private PlayMusicContract.PlayPresenter mPlayPresenter;
    private NotificationManager mNotificationManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        if (mPlayPresenter == null) {
            mPlayPresenter = PlayPresenterImpl.getInstance();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground("音乐app","enjoy musics");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
        
    }

    /**
     * 服务终止时，释放mediaplayer相关资源.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayer mediaPlayer = mPlayPresenter.getMediaPlayer();
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mPlayPresenter = null;
    }
    
    /**
     * 根据安卓版本判断是否要开启通知渠道.
     * */
    public void channelHelper(){
        if(mNotificationManager == null){
            mNotificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mNotificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            if(mNotificationManager != null){
                mNotificationManager.createNotificationChannel(mNotificationChannel);
            }
        }
    }
    
    /**
     * 将服务推到前台服务.
     * */
    public void startForeground(String title, String content){
        channelHelper();
        Notification.Builder builder; 
        //若安卓版本大于8.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this,CHANNEL_ID);
        }else{
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_LOW);
        }
        if(title != null){
            builder.setContentTitle(title);
        }
        if(content != null){
            builder.setContentText(content);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(1,builder.build());
    }
}

