package com.example.www11.mymusicplayer.playmusic;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.adapter.PlayQueueAdapter;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.util.AsyncImageTask;

import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.LOOP_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.ORDER_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PAUSE;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.PLAY_STATE_STOP;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.RANDOM_PLAY;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.ERROR;
import static com.example.www11.mymusicplayer.util.Constants.PlayMusicConstant.FAIL;

/**
 * 底部"音乐播放栏"的碎片.
 */
public class PlayMusicFragment extends Fragment implements PlayMusicContract.OnView,
        PlayQueueAdapter.InnerItemOnClickListener {

    private View view;

    /**
     * 进度条
     */
    private SeekBar mSeekBar;

    /**
     * 播放或者暂停按钮
     */
    private Button mPlayOrPause;

    private PlayMusicContract.Presenter mPlayPresenter;

    /**
     * 用户是否触碰了进度条
     */
    private boolean mUserTouchProgress = false;

    private ImageButton mPlayNext;
    private ImageButton mPlayPre;
    private TextView mSingerName;
    private TextView mMusicName;
    private ImageView mMusicPicture;
    private ImageButton mPlayModeBtn;

    /**
     * 记录播放的方式，默认是列表循环
     */
    private int mPlayMode = ORDER_PLAY;

    /**
     * 音乐播放队列的按钮点击.
     */
    private ImageButton mPlayQueue;

    /**
     * 自定义的Dialog，从底部弹出一个播放列表.
     */
    private Dialog mQueueDialog;

    /**
     * 播放队列所对应的listView
     */
    private ListView mQueueList;

    /**
     * 音乐播放队列所对应的view.
     */
    private View mPlayQueueView;

    /**
     * 播放队列的listView对应的适配器.
     */
    private PlayQueueAdapter mAdapter;

    /**
     * 标记最近一次的音乐播放位置.
     */
    private int mLastPosition;

    /**
     * 播放队列中的"下一首"播放按钮.
     */
    private Button mPlayNextBtn;

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
     * 主要作用是恢复底部的ui.
     * */
    @Override
    public void onResume() {
        super.onResume();
        if (mPlayPresenter != null && mPlayPresenter.getMusics().size() != 0) {
            //如果服务开着，活动销毁，再次打开活动时恢复底部播放栏的UI状态
            Music music = mPlayPresenter.getMusics().get(mPlayPresenter.getCurrentPosition());
            showMusicInfo(music);//恢复歌曲信息
            onPlayStateChange(mPlayPresenter.getPlayState());//恢复"播放"或者"暂停"按钮
            //恢复播放模式
            mPlayMode = mPlayPresenter.getPlayMode();
            switch (mPlayMode) {
                case RANDOM_PLAY:
                    mPlayModeBtn.setBackgroundResource(R.drawable.random_play);
                    break;
                case LOOP_PLAY:
                    mPlayModeBtn.setBackgroundResource(R.drawable.loop_play);
                    break;
                case ORDER_PLAY:
                    mPlayModeBtn.setBackgroundResource(R.drawable.order_play);
                    break;
            }
        }
    }

    /**
     * 初始化数据.
     */
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
        
        mPlayQueue = view.findViewById(R.id.play_queue);

        mPlayQueueView = View.inflate(getActivity(), R.layout.play_queue, null);
        mQueueList = mPlayQueueView.findViewById(R.id.queue_list);
        mPlayNextBtn = mPlayQueueView.findViewById(R.id.next_one);
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

        mPlayQueue.setOnClickListener(v -> showBottomDialog());

        mPlayModeBtn.setOnClickListener(v -> {
            switch (mPlayMode) {
                case ORDER_PLAY:
                    mPlayPresenter.changePlayMode(RANDOM_PLAY);
                    break;
                case RANDOM_PLAY:
                    mPlayPresenter.changePlayMode(LOOP_PLAY);
                    break;
                case LOOP_PLAY:
                    mPlayPresenter.changePlayMode(ORDER_PLAY);
                    break;
            }
        });

        mPlayNext.setOnClickListener(v -> mPlayPresenter.playNext());
        mPlayPre.setOnClickListener(v -> mPlayPresenter.playPre());
        mPlayNextBtn.setOnClickListener(v -> mPlayPresenter.playNext());
    }

    /**
     * 用户点击歌单中的歌曲时，MainActivity调用该方法
     */
    public void playMusics(List<Music> musics, int position) {
        mPlayPresenter.playMusic(musics, position);
    }

    /**
     * 从底部弹出一个列表.
     */
    private void showBottomDialog() {
        if (getActivity() != null && mQueueDialog == null) {
            mQueueDialog = new Dialog(getActivity());

            mQueueDialog.setContentView(mPlayQueueView);//设置dialog对应的视图

            Window window = mQueueDialog.getWindow();//得到dialog的窗口
            //设置窗口位置和大小等信息
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();//可以得到屏幕分辨率等信息
                window.getWindowManager().getDefaultDisplay().getMetrics(dm);//传入屏幕的信息
                int screenHeight = dm.heightPixels;//获取屏幕的高度
                layoutParams.height = screenHeight / 2;//设置高度
                window.setAttributes(layoutParams);
            }
        }

        //设置listView的数据
        if (getActivity() != null) {
            mAdapter = new PlayQueueAdapter(getActivity(), R.layout.play_queue_item,
                    mPlayPresenter.getMusics());
            mAdapter.setCurrentPosition(mPlayPresenter.getCurrentPosition());
            mAdapter.setListener(this);//设置回调接口
            mLastPosition = mPlayPresenter.getCurrentPosition();//记录最近一次的音乐播放位置
            mQueueList.setAdapter(mAdapter);
        }

        //展示窗口
        mQueueDialog.show();
    }

    /**
     * 播放队列更新数据.
     * <p>
     * 这里在播放下一首音乐时，如果播放队列是开着的，要实时更新数据，即改变播放的音乐展示红色，其他展示
     * 黑色，这里将音乐的某个成员变量与空字符串做拼接，之后在notifyDataSetChanged()则会更新相应的数据.
     * </p>
     */
    @Override
    public void changeDialogData() {
        //为播放队列设置数据源
        if (getActivity() != null && mQueueDialog != null) {
            if (mQueueDialog.isShowing()) {
                List<Music> musics = mPlayPresenter.getMusics();//得到当前正在播放的音乐列表
                Music music = musics.get(mLastPosition);//要更新的数据
                music.setName(music.getName() + "");
                int currentPosition = mPlayPresenter.getCurrentPosition();
                music = musics.get(currentPosition);//要更新的数据
                music.setName(music.getName() + "");
                mAdapter.setCurrentPosition(currentPosition);//设置当前的音乐播放位置
                mLastPosition = currentPosition;//记录最新一次的音乐播放位置
                mAdapter.notifyDataSetChanged();//更新数据
            }
        }
    }

    /**
     * 播放模式改变时，回调这个方法.
     * */
    @Override
    public void showPlayMode(int playMode) {
        switch (playMode) {
            case RANDOM_PLAY:
                Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT).show();
                mPlayModeBtn.setBackgroundResource(R.drawable.random_play);
                mPlayMode = RANDOM_PLAY;
                break;
            case LOOP_PLAY:
                Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT).show();
                mPlayModeBtn.setBackgroundResource(R.drawable.loop_play);
                mPlayMode = LOOP_PLAY;
                break;
            case ORDER_PLAY:
                Toast.makeText(getActivity(), "列表循环", Toast.LENGTH_SHORT).show();
                mPlayModeBtn.setBackgroundResource(R.drawable.order_play);
                mPlayMode = ORDER_PLAY;
                break;
        }
    }


    /**
     * view接口，调整播放状态的UI.
     */
    @Override
    public void onPlayStateChange(int state) {
        switch (state) {
            //音乐在播放中，就把UI设置为"暂停"
            case PLAY_STATE_PLAY:
                mPlayOrPause.setText("暂停");
                break;
            //音乐停止，就把UI设置为"播放"
            //音乐暂停，就把UI设置为"播放"
            case PLAY_STATE_PAUSE:
            case PLAY_STATE_STOP:
                mPlayOrPause.setText("播放");
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
     */
    @Override
    public void showMusicInfo(Music music) {
        mMusicName.setText(music.getName());
        mSingerName.setText(music.getSingerName());
        AsyncImageTask down = new AsyncImageTask(drawable -> {
            if (drawable != null) {
                mMusicPicture.setImageDrawable(drawable);
            }
        });
        down.execute(music.getPicUrl());
    }

    /**
     * 播放音乐出现错误时.
     */
    @Override
    public void showError() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "播放出现错误,自动为您播放下一首",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showFail(String msg) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), msg,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 当播放队列的xx被点击时，会回调这个方法.
     */
    @Override
    public void itemClick(View v) {
        int position = (int) v.getTag();//获得被点击的item的位置
        if (v.getId() == R.id.remove_music) {
            if (position < mLastPosition) {
                //如果被点击的item位于正在播放的item的上方
                mAdapter.setCurrentPosition(mLastPosition - 1);
                mLastPosition -= 1;
                mPlayPresenter.setCurrentPosition(mPlayPresenter.getCurrentPosition() - 1);
                mPlayPresenter.getMusics().remove(position);
                mAdapter.notifyDataSetChanged();
            } else if (position > mLastPosition) {
                //如果被点击的item位于正在播放的item的下方
                mPlayPresenter.getMusics().remove(position);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
