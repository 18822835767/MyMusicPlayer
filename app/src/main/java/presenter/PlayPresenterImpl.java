package presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageButton;

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


    private PlayMusicContract.OnPlayView mOnPlayView;//view接口的引用
    private int currentState = PLAY_STATE_STOP;//表示目前的播放状态，初始是停止播放的状态
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;//定时器，每个一段时间通过回调更新UI
    private SeekTimeTask mSeekTimeTask;
    private List<String> mMusics = new ArrayList<>();

    private boolean mFirstPlay = true;
    private int mCurrentPosition = 0;//表示当前的播放位置

    @Override
    public void playOrPause() {
        switch (currentState) {
            case PLAY_STATE_STOP:
                File file = new File("/sdcard/music.mp3");
                mMusics.add(file.getPath());
                mMusics.add(file.getPath());
                mMusics.add("/sdcard/music2.mp3");

                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                initMediaPlayerData(mMusics.get(mCurrentPosition));
                
                currentState = PLAY_STATE_PLAY;

                break;
            case PLAY_STATE_PLAY:
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    currentState = PLAY_STATE_PAUSE;
                    stopTimer();
                }
                break;
            case PLAY_STATE_PAUSE:
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                    currentState = PLAY_STATE_PLAY;
                    startTimer();
                }
                break;
            default:
                break;
        }

        //回调view接口方法，通知界面播放状态的更新
        if (mOnPlayView != null) {
            mOnPlayView.onPlayStateChange(currentState);
        }
    }

    /**
     * 设置MediaPlayer的信息
     */
    private void initMediaPlayerData(String dataSource) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(dataSource);
                mMediaPlayer.prepareAsync();
                //监听
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnErrorListener(this);
                mMediaPlayer.setOnCompletionListener(this);
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
        if (mMediaPlayer != null) {
            //这里时用户拖动进度条，currentProcess表示拖动后音乐处于的播放时间点
            int currentProcess = (int) ((seek * 1.0f / 100) * mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(currentProcess);
        }
    }

    @Override
    public void registOnPlayView(PlayMusicContract.OnPlayView onPlayView) {
        this.mOnPlayView = onPlayView;
    }

    /**
     * 当音乐在播放的时候，开启TimerTask
     */
    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mSeekTimeTask == null) {
            mSeekTimeTask = new SeekTimeTask();
        }
        mTimer.schedule(mSeekTimeTask, 0, 300);//定时任务，周期为300ms
    }

    /**
     * 当音乐暂停时，停止TimerTask
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mSeekTimeTask != null) {
            mSeekTimeTask.cancel();
            mSeekTimeTask = null;
        }
    }

    /**
     * 监听，当MediaPlayer播放完一首音乐后.
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mFirstPlay = false;
        mCurrentPosition++;
        if (mMusics != null && mCurrentPosition < mMusics.size()) {
            mOnPlayView.onSeekChange(0);
            initMediaPlayerData(mMusics.get(mCurrentPosition));
        }
    }

    /**
     * 监听，当MediaPlayer播放播放出错时.
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mOnPlayView.showError();
        mOnPlayView.onPlayStateChange(currentState);
        return false;
    }

    /**
     * 监听，当MediaPlayer装载好播放的媒体资源时.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
            if (mFirstPlay) {
                startTimer();
            }
        }
    }

    private class SeekTimeTask extends TimerTask {
        @Override
        public void run() {
            if (mMediaPlayer != null && mOnPlayView != null) {
                //目前的播放时长
                int currentTimePoint = mMediaPlayer.getCurrentPosition();
                //目前的播放的百分比进度
                int currentPosition = (int) (currentTimePoint * 1.0f * 100 / mMediaPlayer.getDuration());
                //回调view接口更新进度条
                mOnPlayView.onSeekChange(currentPosition);
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void playNext() {
        
    }

    @Override
    public void playPre() {

    }

}

