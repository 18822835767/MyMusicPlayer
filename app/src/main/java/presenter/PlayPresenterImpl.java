package presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;


import java.util.Timer;
import java.util.TimerTask;

import contract.PlayMusicContract;

public class PlayPresenterImpl implements PlayMusicContract.PlayPresenter {

    private PlayMusicContract.OnPlayView onPlayView;//view接口的引用
    private int currentState = PLAY_STATE_STOP;//表示目前的播放状态，初始是停止播放的状态
    private MediaPlayer mediaPlayer;
    private Timer timer;//定时器，每个一段时间通过回调更新UI
    private SeekTimeTask seekTimeTask;

    public PlayPresenterImpl(PlayMusicContract.OnPlayView onPlayView){
        this.onPlayView = onPlayView;
    }

    @Override
    public void playOrPause() {
        switch (currentState){
            case PLAY_STATE_STOP:
                //停止状态，先初始化播放器.
                initPlayer();

                if(mediaPlayer != null){
                    try {
                        mediaPlayer.setDataSource("http://m7.music.126.net/20200512102020/08cd09a" +
                                "8fd50dc354521335ab16253f2/ymusic/946c/0a6f/4474/217aad7ed820d5f19bc" +
                                "a2dcecddda221.mp3");   
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        currentState = PLAY_STATE_PLAY;
                        startTimer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PLAY_STATE_PLAY:
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                    currentState = PLAY_STATE_PAUSE;
                    stopTimer();
                }
                break;
            case PLAY_STATE_PAUSE:
                if(mediaPlayer != null){
                    mediaPlayer.start();
                    currentState = PLAY_STATE_PLAY;
                    startTimer();
                }
                break;
            default:
                break;
        }

        //回调view接口方法，通知界面播放状态的更新
        if(onPlayView != null){
            onPlayView.onPlayStateChange(currentState);
        }
    }

    /**
     * @param  seek 表示进度条播放位置，总共分为100份
     * */
    @Override
    public void seekTo(int seek) {
        if(mediaPlayer != null){
            //这里时用户拖动进度条，currentProcess表示拖动后音乐处于的播放时间点
            int currentProcess = (int)((seek * 1.0f /100)*mediaPlayer.getDuration());
            mediaPlayer.seekTo(currentProcess);
        }
    }

    /**
     * 初始化播放器.
     * */
    private void initPlayer(){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    /**
     * 当音乐在播放的时候，开启TimerTask
     * */
    private void startTimer(){
        if(timer == null){
            timer = new Timer();
        }
        if(seekTimeTask == null){
            seekTimeTask = new SeekTimeTask();
        }
        timer.schedule(seekTimeTask,0,500);//定时任务，周期为500ms
    }

    /**
     * 当音乐暂停时，停止TimerTask
     * */
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(seekTimeTask != null){
            seekTimeTask.cancel();
            seekTimeTask = null;
        }
    }

    private class SeekTimeTask extends TimerTask {
        @Override
        public void run() {
            if(mediaPlayer != null && onPlayView != null){
                //目前的播放时长
                int currentTimePoint = mediaPlayer.getCurrentPosition();
                //目前的播放的百分比进度
                int currentPosition = (int) (currentTimePoint * 1.0f * 100/mediaPlayer.getDuration());
                //回调view接口更新进度条
                onPlayView.onSeekChange(currentPosition);
            }
        }
    }
}

