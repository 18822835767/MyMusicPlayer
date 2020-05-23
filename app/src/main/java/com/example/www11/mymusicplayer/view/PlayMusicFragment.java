package com.example.www11.mymusicplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.R;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.www11.mymusicplayer.contract.PlayMusicContract;
import com.example.www11.mymusicplayer.entity.Music;
import com.example.www11.mymusicplayer.presenter.PlayPresenterImpl;
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
public class PlayMusicFragment extends Fragment implements PlayMusicContract.OnView {

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
    private Handler mHandler;

    /**
     * 记录播放的方式，默认是列表循环
     */
    private int mPlayMode = ORDER_PLAY;

//    private ImageButton mPlayQueue;//音乐播放队列的按钮点击
//    private PopupWindow mPopWindow;//弹出的播放列表管理


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

        mHandler = new UIHandler(this);
//        mPlayQueue = view.findViewById(R.id.play_queue);
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

//        mPlayQueue.setOnClickListener(v -> showPopupWindow());

        mPlayModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mPlayMode) {
                    case ORDER_PLAY:
                        mPlayPresenter.changePlayMode(RANDOM_PLAY);
                        Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT).show();
                        mPlayModeBtn.setBackgroundResource(R.drawable.random_play);
                        mPlayMode = RANDOM_PLAY;
                        break;
                    case RANDOM_PLAY:
                        mPlayPresenter.changePlayMode(LOOP_PLAY);
                        Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT).show();
                        mPlayModeBtn.setBackgroundResource(R.drawable.loop_play);
                        mPlayMode = LOOP_PLAY;
                        break;
                    case LOOP_PLAY:
                        mPlayPresenter.changePlayMode(ORDER_PLAY);
                        Toast.makeText(getActivity(), "列表循环", Toast.LENGTH_SHORT).show();
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
     */
    void playMusics(List<Music> musics, int position) {
        mPlayPresenter.playMusic(musics, position);
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
        Message message = Message.obtain();
        message.what = ERROR;
        mHandler.sendMessage(message);
    }

    @Override
    public void showFail(String msg) {
        Message message = Message.obtain();
        message.what = FAIL;
        message.obj = msg;
        mHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    /**
//     * 用户点击播放列表管理时弹出一个popupwindow.
//     * */
//    private void showPopupWindow(){
//        //弹出列表的内容
//        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.play_queue,null);
//        mPopWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        
//        mPopWindow.setBackgroundDrawable(new ColorDrawable());
//        mPopWindow.setOutsideTouchable(true);
//        mPopWindow.setOutsideTouchable(true);
//        
//        mPopWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
//    }

    private static class UIHandler extends Handler {
        WeakReference<PlayMusicFragment> mWeakReference;

        UIHandler(PlayMusicFragment playMusicFragment) {
            mWeakReference = new WeakReference<>(playMusicFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            PlayMusicFragment fragment = mWeakReference.get();
            Activity activity = null;
            if (fragment != null) {
                activity = fragment.getActivity();
            }
            if (fragment != null) {
                switch (msg.what) {
                    case FAIL:
                        String message = (String) msg.obj;
                        if (activity != null) {
                            Toast.makeText(fragment.getActivity(), message,
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ERROR:
                        if (activity != null) {
                            Toast.makeText(fragment.getActivity(), "播放出现错误,自动为您播放下一首",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
