package com.example.www11.mymusicplayer.presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.www11.mymusicplayer.contract.PlayMusicContract;
import com.example.www11.mymusicplayer.entity.Music;


import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.ORDER_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PAUSE;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_STOP;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.RANDOM_PLAY;


/**
 * 因为要使播放器在服务中，又使播放器可以收到PlayMusicFragment，所以采取单例模式.
 */
public class PlayPresenterImpl implements PlayMusicContract.Presenter,
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
    
    /**
     * view接口的引用
     */
    private PlayMusicContract.OnView mOnPlayView;
    
    /**
     * 表示目前的播放状态，初始是停止播放的状态
     * */
    private int mCurrentState = PLAY_STATE_STOP;
    
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    /**
     * 定时器，每个一段时间通过回调更新UI
     * */
    private Timer mTimer;
    
    /**
     * 定时任务
     * */
    private SeekTimeTask mSeekTimeTask;
    
    /**
     * 存放要播放音乐的列表
     * */
    private List<Music> mMusics = new ArrayList<>();

    /**
     * 是否是第一次点击播放
     * */
    private boolean mFirstPlay = true;
    
    /**
     * 判断用户是否调用了mediaPlayer的reset()
     * */
    private boolean mReset = false;
    
    /**
     * 表示当前的播放位置
     * */
    private int mCurrentPosition = 0;
    
    /**
     * 记录播放的方式，默认是列表循环
     * */
    private int mPlayMode = ORDER_PLAY;

    @Override
    public void playOrPause() {
        if (mMusics.size() == 0) {
            mOnPlayView.showFail("当前没有歌哦");
            return;
        }
        switch (mCurrentState) {
            //表示第一次播放时
            case PLAY_STATE_STOP:
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(mMusics.get(mCurrentPosition).getMusicURL());
                    mMediaPlayer.prepareAsync();
                    //监听
                    mMediaPlayer.setOnPreparedListener(this);
                    mMediaPlayer.setOnErrorListener(this);
                    mMediaPlayer.setOnCompletionListener(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mCurrentState = PLAY_STATE_PLAY;
                startTimer();
                mFirstPlay = false;
                break;
            //从播放到暂停
            case PLAY_STATE_PLAY:
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    mCurrentState = PLAY_STATE_PAUSE;
                    stopTimer();
                }
                break;
            //从暂停到播放
            case PLAY_STATE_PAUSE:
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                    mCurrentState = PLAY_STATE_PLAY;
                    startTimer();
                }
                break;
            default:
                break;
        }

        //回调view接口方法，通知界面播放状态的更新
        if (mOnPlayView != null) {
            mOnPlayView.onPlayStateChange(mCurrentState);
        }
    }

    /**
     * 设置MediaPlayer的信息，给歌的地址，放歌.
     */
    private void initMediaPlayerData(String dataSource) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        mReset = true;
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

    /**
     * 监听，当MediaPlayer播放完一首音乐后,自动播放下一首.
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    /**
     * 监听，当MediaPlayer播放播放出错时.
     * <p>
     * 如果是因为调用了reset()而出错，则不处理.
     * 如果是因为播放出错，则给提示信息，自动播放下一首.
     * </p>
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (!mReset) {
            //mediaPlayer没有经过了reset()，正常出错
            mOnPlayView.showError();
            mOnPlayView.onPlayStateChange(mCurrentState);
            playNext();
        } else {
            //调用了reset(),实际并没有出错
            mReset = false;
        }
        return true;
    }

    /**
     * 监听，当MediaPlayer装载好播放的媒体资源时.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
            mOnPlayView.showMusicInfo(mMusics.get(mCurrentPosition));//播放栏中显示歌曲的信息
            mCurrentState = PLAY_STATE_PLAY;
            mOnPlayView.onPlayStateChange(mCurrentState);
        }
    }

    /**
     * 用户点击时，播放下一首歌曲.
     */
    @Override
    public void playNext() {
        if (mMusics.size() == 0) {
            mOnPlayView.showFail("当前没有歌哦");
            return;
        }

        switch (mPlayMode) {
            //列表循环播放
            case ORDER_PLAY:
                if (mCurrentPosition == mMusics.size() - 1) {
                    mCurrentPosition = 0;
                } else {
                    mCurrentPosition++;
                }
                break;
            //随机播放
            case RANDOM_PLAY:
                mCurrentPosition = (int) (Math.random() * mMusics.size());
                break;
            //单曲循环不用做处理
            default:
                break;
        }

        initMediaPlayerData(mMusics.get(mCurrentPosition).getMusicURL());
        mOnPlayView.changeDialogData();//通知播放队列更新数据
    }

    /**
     * 用户点击时，播放上一首歌曲.
     */
    @Override
    public void playPre() {
        if (mMusics.size() == 0) {
            mOnPlayView.showFail("当前没有歌哦");
            return;
        }

        if (mCurrentPosition == 0) {
            mCurrentPosition = mMusics.size() - 1;
        } else {
            mCurrentPosition--;
        }

        initMediaPlayerData(mMusics.get(mCurrentPosition).getMusicURL());
    }

    /**
     * 用户点击歌单中的某首歌播放时，主活动会调用这个方法.
     */
    @Override
    public void playMusic(List<Music> musics, int position) {
        mMusics = musics;
        mCurrentPosition = position;

        if (mFirstPlay) {
            playOrPause();
        } else {
            initMediaPlayerData(musics.get(position).getMusicURL());
        }
    }


    /**
     * @param seek 表示进度条播放位置，进度条总共分为100份
     */
    @Override
    public void seekTo(int seek) {
        if (mMediaPlayer != null) {
            //这里时用户拖动进度条，currentProcess表示拖动后音乐处于的播放时间点
            int currentProcess = (int) ((seek * 1.0f / 100) * mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(currentProcess);
        }
    }

    /**
     * 为PlayPresenter注册view接口.
     */
    @Override
    public void registOnPlayView(PlayMusicContract.OnView onPlayView) {
        this.mOnPlayView = onPlayView;
    }

    public void changePlayMode(int mode){
        mPlayMode = mode;
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
     * 更新进度条的位置.
     */
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

    public List<Music> getMusics() {
        return mMusics;
    }

    @Override
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

}

