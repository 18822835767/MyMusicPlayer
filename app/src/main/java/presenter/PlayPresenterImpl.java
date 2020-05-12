package presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import contract.PlayMusicContract;

public class PlayPresenterImpl implements PlayMusicContract.PlayPresenter,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
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
    private List<String> musics = new ArrayList<>();

    private boolean firstPlay = true;
    private int currentPosition = 0;//表示当前的播放位置

    @Override
    public void playOrPause() {
        switch (currentState) {
            case PLAY_STATE_STOP:
                File file = new File("/sdcard/music.mp3");
                musics.add(file.getPath());
                musics.add(file.getPath());
                musics.add(file.getPath());

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                initMediaPlayerData(musics.get(currentPosition));
                
                currentState = PLAY_STATE_PLAY;

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
     * 设置MediaPlayer的信息
     */
    private void initMediaPlayerData(String dataSource) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(dataSource);
                mediaPlayer.prepareAsync();
                //监听
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        timer.schedule(seekTimeTask, 0, 300);//定时任务，周期为300ms
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

    /**
     * 监听，当MediaPlayer播放完一首音乐后.
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        firstPlay = false;
        currentPosition++;
        if (musics != null && currentPosition < musics.size()) {
            onPlayView.onSeekChange(0);
            initMediaPlayerData(musics.get(currentPosition));
        }
    }

    /**
     * 监听，当MediaPlayer播放播放出错时.
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        onPlayView.showError();
        onPlayView.onPlayStateChange(currentState);
        return false;
    }

    /**
     * 监听，当MediaPlayer装载好播放的媒体资源时.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
            if (firstPlay) {
                startTimer();
            }
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}

