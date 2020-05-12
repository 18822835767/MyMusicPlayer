package view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.mymusicplayer.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import contract.PlayMusicContract;
import presenter.PlayPresenterImpl;


/**
 * "音乐播放栏"的碎片.
 */
public class MusicBarFragment extends Fragment implements PlayMusicContract.OnPlayView {

    private View view;
    private SeekBar seekBar;
    private ImageButton playOrPause;
    private PlayMusicContract.PlayPresenter playPresenter;
    private boolean userTouchProgress = false;//用户是否触碰了进度条
    
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
        seekBar = view.findViewById(R.id.seek_bar);
        playOrPause = view.findViewById(R.id.play_or_pause);
        
        playPresenter = new PlayPresenterImpl(this);
    }

    private void initEvent() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条发生改变时   
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //用户开始触摸进度条时
                userTouchProgress = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户释放进度条时
                int touchProgress = seekBar.getProgress();

                if (playPresenter != null) {
                    playPresenter.seekTo(touchProgress);
                }
                userTouchProgress = true;
            }
        });

        playOrPause.setOnClickListener(v -> {
            if (playPresenter != null) {
                playPresenter.playOrPause();
            }
        });

    }

    /**
     * view接口，调整播放状态的UI.
     */
    @Override
    public void onPlayStateChange(int state) {
        switch (state) {
            case PlayMusicContract.PlayPresenter.PLAY_STATE_PLAY:
                //音乐在播放中，就把UI设置为"暂停"
                playOrPause.setBackgroundResource(R.drawable.music_pause);
                break;
            case PlayMusicContract.PlayPresenter.PLAY_STATE_PAUSE:
                //音乐暂停，就把UI设置为"播放"
                playOrPause.setBackgroundResource(R.drawable.music_play);
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
        if (!userTouchProgress) {
            seekBar.setProgress(seek);
        }
    }

}
