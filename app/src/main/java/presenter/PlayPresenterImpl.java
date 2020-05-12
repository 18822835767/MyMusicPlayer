package presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;


import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import contract.PlayMusicContract;

public class PlayPresenterImpl implements PlayMusicContract.PlayPresenter {
    private volatile static PlayPresenterImpl instance = null;

    private PlayPresenterImpl() {
    }

    public static PlayPresenterImpl getInstance() {
        if (instance == null) {
            synchronized (PlayPresenterImpl.class) {
                if (instance == null) {
                    instance = new PlayPresenterImpl();
                }
            }
        }
        return instance;
    }


    private PlayMusicContract.OnPlayView onPlayView;//view接口的引用
    private int currentState = PLAY_STATE_STOP;//表示目前的播放状态，初始是停止播放的状态
    private MediaPlayer mediaPlayer;
    private Timer timer;//定时器，每个一段时间通过回调更新UI
    private SeekTimeTask seekTimeTask;
    
    @Override
    public void playOrPause() {
        switch (currentState) {
            case PLAY_STATE_STOP:
                //停止状态，先初始化播放器.
                initPlayer();

                if (mediaPlayer != null) {
                    try {
                        try {
                            File file = new File("/sdcard/music.mp3");
                            mediaPlayer.setDataSource(file.getPath());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
                            currentState = PLAY_STATE_PLAY;
                            startTimer();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PLAY_STATE_PLAY:
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    currentState = PLAY_STATE_PAUSE;
                    stopTimer();
                }
                break;
            case PLAY_STATE_PAUSE:
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    currentState = PLAY_STATE_PLAY;
                    startTimer();
                }
                break;
            default:
                break;
        }

        //回调view接口方法，通知界面播放状态的更新
        if (onPlayView != null) {
            onPlayView.onPlayStateChange(currentState);
        }
    }

    /**
     * @param seek 表示进度条播放位置，总共分为100份
     */
    @Override
    public void seekTo(int seek) {
        if (mediaPlayer != null) {
            //这里时用户拖动进度条，currentProcess表示拖动后音乐处于的播放时间点
            int currentProcess = (int) ((seek * 1.0f / 100) * mediaPlayer.getDuration());
            mediaPlayer.seekTo(currentProcess);
        }
    }

    @Override
    public void registOnPlayView(PlayMusicContract.OnPlayView onPlayView) {
        this.onPlayView = onPlayView;
    }

    @Override
    public void unRegistOnPlayView() {
        onPlayView = null;
    }

    /**
     * 初始化播放器.
     */
    private void initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    /**
     * 当音乐在播放的时候，开启TimerTask
     */
    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (seekTimeTask == null) {
            seekTimeTask = new SeekTimeTask();
        }
        timer.schedule(seekTimeTask, 0, 500);//定时任务，周期为500ms
    }

    /**
     * 当音乐暂停时，停止TimerTask
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (seekTimeTask != null) {
            seekTimeTask.cancel();
            seekTimeTask = null;
        }
    }

    private class SeekTimeTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer != null && onPlayView != null) {
                //目前的播放时长
                int currentTimePoint = mediaPlayer.getCurrentPosition();
                //目前的播放的百分比进度
                int currentPosition = (int) (currentTimePoint * 1.0f * 100 / mediaPlayer.getDuration());
                //回调view接口更新进度条
                onPlayView.onSeekChange(currentPosition);
            }
        }
    }
}

