package view;

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
import contract.PlayMusicContract;
import entity.Music;
import presenter.PlayPresenterImpl;
import util.DownImage;


/**
 * 底部"音乐播放栏"的碎片.
 */
public class PlayMusicFragment extends Fragment implements PlayMusicContract.OnPlayView {

    private View view;
    private SeekBar mSeekBar;
    private ImageButton mPlayOrPause;
    private PlayMusicContract.PlayPresenter mPlayPresenter;
    private boolean mUserTouchProgress = false;//用户是否触碰了进度条
    private ImageButton mPlayNext;
    private ImageButton mPlayPre;
    private TextView mSingerName;
    private TextView mMusicName;
    private ImageView mMusicPicture; 
    
    private final int ERROR = 0;
    private final int SUCCESS = 1;
    private final int FAIL = 2;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_bar, container, false);

        initData();
        initEvent();

        return view;
    }

    private void initData() {
        mSeekBar = view.findViewById(R.id.seek_bar);
        mPlayOrPause = view.findViewById(R.id.play_or_pause);
        mPlayNext = view.findViewById(R.id.next_one);
        mPlayPre = view.findViewById(R.id.pre_one);
        mSingerName = view.findViewById(R.id.singer_name);
        mMusicName = view.findViewById(R.id.music_name);
        mMusicPicture = view.findViewById(R.id.music_picture);
        
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
                    mPlayPresenter.seekTo(touchProgress);
                }
                mUserTouchProgress = false;
            }
        });

        mPlayOrPause.setOnClickListener(v -> {
            if (mPlayPresenter != null) {
                mPlayPresenter.playOrPause();
            }
        });
        
        mPlayNext.setOnClickListener(v -> mPlayPresenter.playNext());
        mPlayPre.setOnClickListener(v -> mPlayPresenter.playPre());

    }
    
    /**
     * 用户点击歌单中的歌曲时，MainActivity调用该方法
     * */
    public void playMusics(List<Music> musics, int position){
        mPlayPresenter.playMusic(musics,position);
    }

    /**
     * view接口，调整播放状态的UI.
     */
    @Override
    public void onPlayStateChange(int state) {
        switch (state) {
            case PlayMusicContract.PlayPresenter.PLAY_STATE_PLAY:
                //音乐在播放中，就把UI设置为"暂停"
                mPlayOrPause.setBackgroundResource(R.drawable.music_pause);
                break;
            case PlayMusicContract.PlayPresenter.PLAY_STATE_PAUSE:
                //音乐暂停，就把UI设置为"播放"
                mPlayOrPause.setBackgroundResource(R.drawable.music_play);
                break;
            case PlayMusicContract.PlayPresenter.PLAY_STATE_STOP:
                //音乐停止，就把UI设置为"播放"
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
//        mMusicPicture.setImageDrawable(DownImage);
    }

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
