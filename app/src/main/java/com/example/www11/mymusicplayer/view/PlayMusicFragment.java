package com.example.www11.mymusicplayer.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.www11.mymusicplayer.contract.PlayMusicContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.presenter.PlayPresenterImpl;
import com.example.www11.mymusicplayer.util.BitmapWorkertask;

import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.LOOP_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.ORDER_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PAUSE;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_STOP;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.RANDOM_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.SUCCESS;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.FAIL;

/**
 * 底部"音乐播放栏"的碎片.
 */
public class PlayMusicFragment extends Fragment implements PlayMusicContract.OnPlayView {

    private View view;
    private SeekBar mSeekBar;//进度条
    private ImageButton mPlayOrPause;//播放或者暂停按钮
    private PlayMusicContract.PlayPresenter mPlayPresenter;
    private boolean mUserTouchProgress = false;//用户是否触碰了进度条
    private ImageButton mPlayNext;
    private ImageButton mPlayPre;
    private TextView mSingerName;
    private TextView mMusicName;
    private ImageView mMusicPicture;
    private ImageButton mPlayModeBtn;
    
    private int mPlayMode = ORDER_PLAY;//记录播放的方式，默认是列表循环
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_bar, container, false);

        initData();
        initEvent();

        return view;
    }

    /**
     * 初始化数据.
     * */
    private void initData() {
        mSeekBar = view.findViewById(R.id.seek_bar);
        mPlayOrPause = view.findViewById(R.id.play_or_pause);
        mPlayNext = view.findViewById(R.id.next_one);
        mPlayPre = view.findViewById(R.id.pre_one);
        mSingerName = view.findViewById(R.id.singer_name);
        mMusicName = view.findViewById(R.id.music_name);
        mMusicPicture = view.findViewById(R.id.music_picture);
        mPlayModeBtn = view.findViewById(R.id.play_mode);
        
        mPlayPresenter = PlayPresenterImpl.getInstance();
        mPlayPresenter.registOnPlayView(this);
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条发生改变时   
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //用户开始触摸进度条时
                mUserTouchProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户释放进度条时
                int touchProgress = seekBar.getProgress();

                if (mPlayPresenter != null) {
                    mPlayPresenter.seekTo(touchProgress);//更新进度条
                }
                mUserTouchProgress = false;
            }
        });

        mPlayOrPause.setOnClickListener(v -> {
            if (mPlayPresenter != null) {
                mPlayPresenter.playOrPause();
            }
        });
        
        mPlayModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mPlayMode){
                    case ORDER_PLAY:
                        mPlayPresenter.changePlayMode(RANDOM_PLAY);
                        Toast.makeText(getActivity(),"随机播放",Toast.LENGTH_SHORT).show();
                        mPlayModeBtn.setBackgroundResource(R.drawable.random_play);
                        mPlayMode = RANDOM_PLAY;
                        break;
                    case RANDOM_PLAY:
                        mPlayPresenter.changePlayMode(LOOP_PLAY);
                        Toast.makeText(getActivity(),"单曲循环",Toast.LENGTH_SHORT).show();
                        mPlayModeBtn.setBackgroundResource(R.drawable.loop_play);
                        mPlayMode = LOOP_PLAY;
                        break;
                    case LOOP_PLAY:
                        mPlayPresenter.changePlayMode(ORDER_PLAY);
                        Toast.makeText(getActivity(),"列表循环",Toast.LENGTH_SHORT).show();
                        mPlayModeBtn.setBackgroundResource(R.drawable.order_play);
                        mPlayMode = ORDER_PLAY;
                        break;
                }
            }
        });
        
        mPlayNext.setOnClickListener(v -> mPlayPresenter.playNext());
        mPlayPre.setOnClickListener(v -> mPlayPresenter.playPre());

    }
    
    /**
     * 用户点击歌单中的歌曲时，MainActivity调用该方法
     * */
    void playMusics(List<Music> musics, int position){
        mPlayPresenter.playMusic(musics,position);
    }

    /**
     * view接口，调整播放状态的UI.
     */
    @Override
    public void onPlayStateChange(int state) {
        switch (state) {
            //音乐在播放中，就把UI设置为"暂停"
            case PLAY_STATE_PLAY:
                mPlayOrPause.setBackgroundResource(R.drawable.music_pause);
                break;
            //音乐停止，就把UI设置为"播放"
            //音乐暂停，就把UI设置为"播放"
            case PLAY_STATE_PAUSE:
            case PLAY_STATE_STOP:
                mPlayOrPause.setBackgroundResource(R.drawable.music_play);
                break;
            default:
                break;
        }
    }

    /**
     * 调整播放进度条的UI
     */
    @Override
    public void onSeekChange(int seek) {
        if (!mUserTouchProgress) {
            mSeekBar.setProgress(seek);
        }
    }

    /**
     * 底部播放栏，展示歌曲的信息：专辑图片，歌手名字，歌名.
     * */
    @Override
    public void showMusicInfo(Music music) {
        mMusicName.setText(music.getName());
        mSingerName.setText(music.getSingerName());
        BitmapWorkertask task = new BitmapWorkertask(mMusicPicture.getWidth(),
                mMusicPicture.getHeight(), new BitmapWorkertask.ImageCallback() {
            @Override
            public void getDrawable(Drawable drawable) {
                if(drawable != null){
                    mMusicPicture.setImageDrawable(drawable);
                }
            }
        });
        task.execute(music.getPicUrl());
    }

    /**
     * 播放音乐出现错误时.
     * */
    @Override
    public void showError() {
        Message message = Message.obtain();
        message.what = ERROR;
        handler.sendMessage(message);
    }

    @Override
    public void showFail(String msg) {
        Message message = Message.obtain();
        message.what = FAIL;
        message.obj = msg;
        handler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //todo 这里使用callback是否是想解决什么问题
    // 想一下为什么会出现这个问题
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), "播放出现错误,自动为您播放下一首",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}
